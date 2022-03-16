package aws;

import com.tracelink.dnp.api.exceptions.BadRequestException;
import com.tracelink.dnp.sdk.context.worldview.utils.AwsUtils;
import com.tracelink.dnp.utils.RxUtils;
import com.tracelink.dnp.utils.functional.Try;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static software.amazon.awssdk.utils.StringUtils.isEmpty;

public class S3Test {

    private S3AsyncClient client;
    private Vertx vertx;
    // todo: make it a list, and change according to the namespace; always use the dynamic one; can assign a value at startup.
    private String DEFAULT_BUCKET = "devkubvir-devtest03-dnp-dynamic";
    private String PATH_PREFIX = "dynamic/default/s3qa-test/zihao/psm/";
    private final Logger logger = LoggerFactory.getLogger("S3Test");

    @Before
    public void before() {
        this.client = S3AsyncClient.builder()
                .credentialsProvider(AwsUtils.getDefaultCredentials())
                .overrideConfiguration(AwsUtils.getOverrides())
                .region(Region.US_EAST_1)
                .build();
        this.vertx = Vertx.vertx();
    }

    @Test
    public void getObjectDoNotDownloadToFile() {

        String bucketName = "devkubvir-devtest03-dnp-dynamic";
        // File
        String objectKey = "putObjectTest";

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(generateKey(objectKey))
                .build();
        CompletableFuture<ResponseBytes<GetObjectResponse>> object = client.getObject(objectRequest, AsyncResponseTransformer.toBytes());

        object.whenComplete((resp, err) -> {
            try {
                if (resp != null) {
                    System.out.println("Object downloaded. Details: "+resp);
                } else {
                    err.printStackTrace();
                }
            } finally {
                // Only close the client when you are completely done with it
                client.close();
            }
        });
        object.join();
    }

    @Test
    public void putObjectTest() {
        String bucketName = "devkubvir-devtest03-dnp-dynamic";
        String key = generateKey("putObjectTest");

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Put the object into the bucket
        CompletableFuture<PutObjectResponse> future = client.putObject(objectRequest, AsyncRequestBody.fromBytes(new byte[0]));
        future.whenComplete((resp, err) -> {
            try {
                if (resp != null) {
                    System.out.println("Object uploaded. Details: " + resp);
                } else {
                    // Handle error
                    err.printStackTrace();
                }
            } finally {
                // Only close the client when you are completely done with it
                client.close();
            }
        });

        future.join();
    }

    @Test
    public void putObjectWithSDKCodeTest() {
        putUnmodeledObjectAsBytes("putObjectWithSDKCodeTest").blockingGet();
    }

    @Test
    public void putPathAndObjectWithSDKCodeTest() {
        putUnmodeledObjectAsBytes("path/pathTest").blockingGet();
    }

    public Single<PutObjectResponse> putUnmodeledObjectAsBytes(String keyRelative) {
        logger.debug("Invoked putObjectAsBytes on S3ClientRx, keyRelative:{}", keyRelative);
        // handle empty relativeKey
        if (isEmpty(keyRelative)) {
            return Single.error(emptyKeyRelativeException());
        }

        String fullKey = generateKey(keyRelative);

        // check if object already exists
        return doesObjectExist(DEFAULT_BUCKET, fullKey)
                .flatMap(objectExists -> {
                    if (objectExists) {
                        return Single.error(keyAlreadyExistsException(keyRelative));
                    }

                    PutObjectRequest.Builder builderRequest = PutObjectRequest.builder()
                            .bucket(DEFAULT_BUCKET)
                            .key(fullKey);

                    // build and execute the request
                    PutObjectRequest request = builderRequest.build();

                    CompletableFuture<PutObjectResponse> response =
                            client.putObject(request, AsyncRequestBody.fromBytes(new byte[0]));
                    return RxUtils.fromFuture(response);
                });
    }

    private String generateKey(String keyRelative) {
        return PATH_PREFIX + keyRelative;
    }

    protected BadRequestException emptyKeyRelativeException() {
        return new BadRequestException("keyRelative cannot be empty");
    }

    protected BadRequestException keyAlreadyExistsException(String keyRelative) {
        return new BadRequestException(String.format("keyRelative %s already exists", keyRelative));
    }

    /*
     * *
     * Internal implementation to check if object exists.
     *
     * NOTE: this should be used for all internal calls, so we aren't double dipping to get bucket or build key
     *
     * @param bucket S3 bucketName
     * @param fullKey prefix + keyRelative
     * @return Boolean
     */
    protected Single<Boolean> doesObjectExist(String bucket, String fullKey) {
        if (!StringUtils.isEmpty(fullKey)) {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(fullKey)
                    .build();

            S3Client syncClient = S3Client.builder()
                    .credentialsProvider(AwsUtils.getDefaultCredentials())
                    .overrideConfiguration(AwsUtils.getOverrides())
                    .region(Region.US_EAST_1)
                    .build();

            return vertx.rxExecuteBlocking(promise -> {
                Try.of(() -> syncClient.getObject(request))
                        .onSuccess(response -> {
                                promise.complete(response);
                                System.out.println("onSuccess");
                        })
                        .onFailure(e -> {
                            promise.fail(e);
                            System.out.println("onFailure");
                        });
            }).flatMapSingle(response -> Single.fromCallable(() -> true))
                    .onErrorResumeNext(ex -> {
                        if (ex instanceof NoSuchKeyException) {
                            return Single.fromCallable(() -> false);
                        } else {
                            return Single.error(ex);
                        }
                    });
        } else {
            return Single.error(new IllegalArgumentException("keyRelative cannot be empty"));
        }
    }

}



