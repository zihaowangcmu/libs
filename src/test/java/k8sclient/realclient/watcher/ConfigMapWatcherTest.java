package k8sclient.realclient.watcher;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ConfigMapWatcherTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigMapWatcherTest.class.getSimpleName());
    //    private static String namespace = "default";
    private static String namespace = "devtest03";
    private static final KubernetesClient client = new DefaultKubernetesClient().inNamespace(namespace);

    public static void main(String[] args) {

        // Latch for Watch termination
        final CountDownLatch isWatchClosed = new CountDownLatch(1);

        Watcher<ConfigMap> watcher = new Watcher<>() {
            @Override
            public void eventReceived(Action action, ConfigMap configMap) {
                String name = configMap.getMetadata().getName();
                if (!name.endsWith("-conf")) {
                    logger.info("ignore configMap: {}", name);
                    return;
                }
                switch (action.name()) {
                    case "ADDED":
                        logger.info("{} got added", name);
                        break;
                    case "DELETED":
                        logger.info("{} got deleted", name);
                        break;
                    case "MODIFIED":
                        logger.info("{} got modified", name);
                        break;
                    default:
                        logger.error("Unrecognized event: {}", action.name());
                }
            }

            @Override
            public void onClose(KubernetesClientException e) {
                logger.info("Closed");
                System.out.println("Closed");
                isWatchClosed.countDown();
            }
        };

        try {
            client.configMaps().watch(watcher);
            // Wait till watch gets closed
            isWatchClosed.await();
        } catch (InterruptedException interruptedException) {
            logger.info( "Thread Interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}
