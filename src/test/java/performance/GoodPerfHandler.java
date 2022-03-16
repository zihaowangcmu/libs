package performance;

import com.arangodb.velocypack.VPackSlice;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tracelink.wv.common.internal.util.SerializationFactory;
import com.tracelink.wv.common.internal.util.SerializationFactoryBuilder;
import com.tracelink.wv.common.model.exception.ErrorInfo;
import com.tracelink.wv.common.model.exception.ErrorType;
import com.tracelink.wv.common.model.tcp.Response;
import com.tracelink.wv.common.model.tcp.ResponseMeta;
import com.tracelink.wv.common.model.tcp.v1.ErrorResponseBodyV1;
import com.tracelink.wv.common.model.tcp.v1.FacetedResponseV1;
import com.tracelink.wv.common.model.tcp.v1.GetClientConfigResponseBodyV1;
import com.tracelink.wv.common.model.tcp.v1.GetSchemaResponseBodyV1;
import com.tracelink.wv.common.model.tcp.v1.ReadResponseBodyV1;
import com.tracelink.wv.common.model.tcp.v1.ResponseBodyV1;
import com.tracelink.wv.common.util.TcpUtils;
import com.tracelink.wv.server.fsm.object.PipelineObject;
import com.tracelink.wv.server.logger.TLDBLogger;
import com.tracelink.wv.server.logger.TLDBLoggerFactory;
import com.tracelink.wv.server.server.LogicalDbManager;
import com.tracelink.wv.server.server.WorldviewServer;
import com.tracelink.wv.server.store.LogicalDb;
import com.tracelink.wv.server.store.TransactionManager;
import com.tracelink.wv.server.util.ThreadUtils;
import com.tracelink.wv.server.wvfsm.SocketResponse;
import com.tracelink.wv.server.wvfsm.event.ClientEvent;
import com.tracelink.wv.server.wvfsm.event.Event;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.vertx.core.json.Json;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.net.NetSocket;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.LabelAndValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.tracelink.wv.server.logger.TLDBLoggerUtils.setLicensePlateAndDbName;
import static com.tracelink.wv.server.util.MetricsUtils.updateCompletedOrErrorMeter;
import static com.tracelink.wv.server.util.MetricsUtils.updateTotalTimeHistogramForEvent;

/**
 * Use Flowable to avoid the queue contention!
 * Compare with BadPerfHandler
 */
public class GoodPerfHandler {

    private final TLDBLogger logger = TLDBLoggerFactory.getLogger(com.tracelink.wv.server.server.ResponseHandler.class);

    private final SerializationFactory serializer = new SerializationFactoryBuilder().build();
    private static GoodPerfHandler INSTANCE = null;
    private final static Object syncInstance = new Object();
    //    private static ExecutorService pullResponseThread = null;
    private final int pullResponseThreadsCount = 10;
    private ExecutorService nettyHandleResponseWorkers;
    private Subject<SocketResponse> pullResponseStream;
    private Flowable<SocketResponse> pullResponseFlowable;
    private Scheduler pullResponseScheduler;

    private GoodPerfHandler() {
        this.pullResponseStream = PublishSubject.<SocketResponse>create().toSerialized();
        pullResponseFlowable = this.pullResponseStream.toFlowable(BackpressureStrategy.BUFFER);
        nettyHandleResponseWorkers = ThreadUtils.initThread("nettyHandleResponseWorkers-%d",
                WorldviewServer.getTlDbConfig().getNettyHandleConnectionWorkerThreadCount());
        var pullResponseWorkers = ThreadUtils.initThread("pullResponseThread-%d", pullResponseThreadsCount);
        pullResponseScheduler = Schedulers.from(pullResponseWorkers);

        startPullResponseChain();
    }

    public static GoodPerfHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (syncInstance) {
                INSTANCE = new GoodPerfHandler();
            }
        }
        return INSTANCE;
    }

    private void startPullResponseChain() {
        pullResponseFlowable
                .flatMap(this::handleResponseEvent)
                .subscribe(result -> { },
                        this::logExceptionForConsumer,
                        () -> handleOnComplete("startPullResponseChain"));
    }

    private void handleOnComplete(String flowableName) {
        logger.info("Response processor [{}] has completed", flowableName);
    }

    private void logExceptionForConsumer(Throwable ex) {
        logger.error("Response chain received error: ", ex);
    }

    private Flowable<SocketResponse> handleResponseEvent(SocketResponse response) {
        logger.setLicensePlate(response.getEvent().getLicensePlate());
        return Flowable.just(response)
                .observeOn(pullResponseScheduler)
                .flatMapCompletable(this::returnSocketResponse)
                .<SocketResponse>toFlowable()
                .doOnError(e -> logger.error("pullResponseThread received error: ", e))
                .onErrorResumeNext(ex -> {
                    return Flowable.just(response);
                });
    }

    public void add(SocketResponse socketResponse) {
        try {
            pullResponseStream.onNext(socketResponse);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("Error at ResponseHandler.take(): ", e);
        }
    }

    public void add(PipelineObject pipelineObject) {
        SocketResponse socketResponse = new SocketResponse(pipelineObject.getEvent(),
                pipelineObject.getResponseBuilder().build());
        add(socketResponse);
    }

    public void add(NetSocket socket, Throwable throwable, ErrorType errorType, String licensePlate) {
        ClientEvent event = ClientEvent.ClientEventBuilder.newInstance()
                .setEvenType(Event.EventType.ERROR)
                .setLicensePlate(licensePlate)
                .setSocket(socket)
                .build();

        com.tracelink.wv.server.msg.Response res = com.tracelink.wv.server.msg.Response.builder()
                .setErrorInfo(errorType, throwable.getMessage())
                .build();

        SocketResponse response = new SocketResponse();
        response.setEvent(event);
        response.setResponse(res);
        add(response);
    }

    /**
     * Return the response to client
     * @param socketResponse to be returned
     */
    private Completable returnSocketResponse(SocketResponse socketResponse) {
        Event event = socketResponse.getEvent();
        if (event == null) {
            logger.clearAll();
            logger.debug("ResponseHandler - Event is null. Will not proceed the response.");
            return Completable.complete();
        }
        String dbName = event.getDatabaseName();
        String lp = event.getLicensePlate();
        setLicensePlateAndDbName(logger, lp, dbName);
        if (!event.requiredResponse()) {
            return Completable.complete();
        }
        // response latch is for internal notification
        if (event.getResponseLatch() != null) {
            event.getResponseLatch().setResponse(socketResponse.getResponse());
            event.getResponseLatch().countDown();
        }

        NetSocket socket = socketResponse.getEvent().getSocket();
        // If socket is null, it means the request is not sent by client.
        // It is a request from the server internally, which should not return anything.
        if (socket == null) {
            return Completable.complete();
        }

        // Build response and do preparation
        Response response = new Response();
        setResponseCode(response, socketResponse);
        Map<String, String> meta = Maps.newHashMap();
        prepareResponseMetadata(meta, socketResponse);
        response.setMeta(meta);

        // TODO: handle response body versioning based client version
        Optional<ResponseBodyV1> bodyOpt = createSocketResponseBody(socketResponse);
        bodyOpt.ifPresent(body -> serializer.get().serialize(body)
                .onSuccess(response::setBody)
                .onFailure(e -> logger.error("failed to set response body", e))
        );

        // handle serialization failure
        if (bodyOpt.isPresent() && response.getBody() == null) {
            handleRequestBodySerializationError(response);
        }

        // Update Metrics
        updateCompletedOrErrorMeter(socketResponse);
        updateTotalTimeHistogramForEvent(socketResponse.getEvent());

        // Send response
        long start = System.currentTimeMillis();
        return sendResponse(socket, response)
                .doOnComplete(() -> WorldviewServer.getServerMetrics()
                        .responseSentWriteSocketTimeMS
                        .update(System.currentTimeMillis() - start)
                )
                .doOnError(e -> {
                    // set logger MDC because this is a new thread
                    setLicensePlateAndDbName(logger, lp, dbName);
                    logger.error("Error occurred when sending Response", e);
                    logger.debug("Detail of the error occurred when sending Response", e);
                });
    }

    public void handleRequestBodySerializationError(Response response) {
        response.setResponseCode(500);
        ErrorInfo errorInfo = new ErrorInfo(ErrorType.INTERNAL_SERVER_ERROR,
                "failed to serialize response content.");
        ErrorResponseBodyV1 errorResponseBody = new ErrorResponseBodyV1(errorInfo);
        serializer.get().serialize(errorResponseBody)
                .onSuccess(response::setBody);
    }

    private static void prepareResponseMetadata(Map<String, String> meta, SocketResponse socketResponse) {
        setPagingInfo(meta, socketResponse);
        setDbTxId(meta, socketResponse);
    }

    private static void setPagingInfo(Map<String, String> meta, SocketResponse socketResponse) {
        socketResponse.getResponse().getPagingInfo().ifPresent(
                pagingInfo -> meta.put(ResponseMeta.PAGING_INFO, pagingInfo));
    }

    private static void setDbTxId(Map<String, String> meta, SocketResponse socketResponse) {
        long dbTxId = socketResponse.getResponse().getDbTxId();
        String dbTxIdStr;
        if (dbTxId > 0) {
            dbTxIdStr = String.valueOf(dbTxId);
        } else {
            dbTxIdStr = String.valueOf(getLastCommitDbTxId(socketResponse.getEvent().getDatabaseName()));
        }
        meta.put(ResponseMeta.DB_TX_ID, dbTxIdStr);
    }

    private static long getLastCommitDbTxId(String dbName) {
        return LogicalDbManager.getInstance().getStartedLogicalDb(dbName)
                .map(LogicalDb::getTransactionManager)
                .map(TransactionManager::getCommittedDbTxId)
                .orElse(0L);
    }

    private static void setResponseCode(Response response, SocketResponse socketResponse) {
        Optional<ErrorInfo> error = socketResponse.getResponse().getErrorInfo();
        if (error.isPresent()) {
            switch (error.get().getType()) {
                case BAD_REQUEST_ERROR:
                case SCHEMA_VALIDATION_ERROR:
                case NOT_UNIQUE_ERROR:
                    response.setResponseCode(400);
                    break;
                case NOT_FOUND_ERROR:
                    response.setResponseCode(404);
                    break;
                case OPTIMISTIC_LOCK_ERROR:
                    response.setResponseCode(409);
                    break;
                case SERVICE_UNAVAILABLE_ERROR:
                    response.setResponseCode(503);
                    break;
                case FORBIDDEN:
                    response.setResponseCode(403);
                    break;
                case INTERNAL_SERVER_ERROR:
                default:
                    response.setResponseCode(500);
                    break;
            }
        } else {
            // TODO : Could be 201 (for create)
            response.setResponseCode(200);
        }
    }

    public Completable sendResponse(NetSocket socket, Response response) {
        // response buffer will always contain length prefix
        Buffer buffer = serializer.get().serialize(response)
                .map(x -> Buffer.buffer(x.getBuffer()))
                .map(TcpUtils::prependSize)
                .orElse(TcpUtils.prependSize(Buffer.buffer()));

        return socket.rxWrite(buffer)
                .observeOn(Schedulers.from(nettyHandleResponseWorkers))
                .doOnComplete((() -> logger.debug("successfully sent response.")));
    }

    private static Optional<ResponseBodyV1> createSocketResponseBody(SocketResponse socketResponse) {
        ResponseBodyV1 body = null;
        Event.EventType eventType = socketResponse.getResponse().getErrorInfo().isPresent() ?
                Event.EventType.ERROR : socketResponse.getEvent().getEventType();
        switch (eventType) {
            /**
             * TODO : Design proper return format.
             */
            case CREATE_DB:
                // all data should be from client side. Relief server.
                break;
            case GET_DB:
                // all data should be from client side. Relief server.
                break;
            case WRITE:
                // all data should be from client side. Relief server.
                break;
            case GET_QUERY_PAGE_INFO:
                // using same logic as READ
            case READ:
                // For now, READ includes getOne(), query(), scan()
                ReadResponseBodyV1.Builder builder = ReadResponseBodyV1.builder();
                Optional<List<VPackSlice>> docsOptional = socketResponse.getResponse().getDocs();
                if (docsOptional.isPresent()) {
                    builder.setDocs(docsOptional.get());
                } else {
                    socketResponse.getResponse().getKeys().map(builder::setKeys);
                }
                // handle faceted search result
                socketResponse.getResponse().getFacets()
                        .map(GoodPerfHandler::createFacetedResponse)
                        .map(builder::setFacets);
                body = builder.build();
                break;
            case GET_CLIENT_CONFIG:
                body = new GetClientConfigResponseBodyV1(socketResponse.getResponse()
                        .getClientConfig()
                        .orElse(null));
                break;
            case ERROR:
                ErrorInfo errorInfo = socketResponse.getResponse().getErrorInfo()
                        .orElse(new ErrorInfo(ErrorType.INTERNAL_SERVER_ERROR, null));
                body = new ErrorResponseBodyV1(errorInfo);
                break;
            case GET_SCHEMAS:
                body = getSchemaResponseBodyV1(socketResponse);
                break;
            default:
                break;
        }
        return Optional.ofNullable(body);
    }

    private static List<FacetedResponseV1> createFacetedResponse(List<FacetResult> facets) {
        return facets.stream()
                .map(facet -> {
                    FacetedResponseV1.Builder builder = FacetedResponseV1.builder()
                            .setIndexName(facet.dim)
                            .setPath(Lists.newArrayList(facet.path))
                            .setMatchedDocsCount(facet.value.longValue())
                            .setChildFacetsCount(facet.childCount);

                    if (facet.labelValues != null) {
                        for (LabelAndValue lv : facet.labelValues) {
                            builder.addChildFacet(lv.label, lv.value.longValue());
                        }
                    }
                    return builder;
                })
                .map(FacetedResponseV1.Builder::build)
                .collect(Collectors.toList());
    }

    private static GetSchemaResponseBodyV1 getSchemaResponseBodyV1(SocketResponse socketResponse) {
        GetSchemaResponseBodyV1.Builder getSchemaResponseBuilder = GetSchemaResponseBodyV1.builder();
        Object payload = socketResponse.getEvent().getPayload();
        if (payload != null) {
            getSchemaResponseBuilder.setSchemas((List<String>) payload);
        } else {
            getSchemaResponseBuilder.setSchemas(new ArrayList<>());
        }
        return getSchemaResponseBuilder.build();
    }
}
