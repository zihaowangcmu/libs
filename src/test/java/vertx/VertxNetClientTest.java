package vertx;

import io.reactivex.Flowable;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.net.NetSocket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class VertxNetClientTest {

    private final Logger logger = LoggerFactory.getLogger(VertxNetClientTest.class);

    private final Vertx vertx = Vertx.vertx();

    @Test
    public void bindAddressForTcpClientTest() throws InterruptedException {
        InetSocketAddress address = InetSocketAddress.createUnresolved("127.0.0.1", 8080);
        buildTcpConnection(address).subscribe();
        buildTcpConnection(address).subscribe();
        Thread.sleep(1000);
    }

    private Flowable<NetSocket> buildTcpConnection(InetSocketAddress address) {
        return vertx.createNetClient()
                .rxConnect(address.getPort(), address.getHostName())
                .doOnSuccess(i -> logger.debug("NettyForwarder - Successfully connected to {}", address.toString()))
                .doOnError(e -> logger.error("NettyForwarder - Failed to connect", e))
                .toFlowable();
    }
}
