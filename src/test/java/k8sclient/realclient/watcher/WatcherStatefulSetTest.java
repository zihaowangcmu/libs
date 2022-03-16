package k8sclient.realclient.watcher;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class WatcherStatefulSetTest {

    private static final Logger logger = LoggerFactory.getLogger(WatcherStatefulSetTest.class.getSimpleName());
//    private static String namespace = "default";
    private static String namespace = "devtest03";
    private static final KubernetesClient client = new DefaultKubernetesClient().inNamespace(namespace);

    public static void main(String[] args) {

        // Latch for Watch termination
        final CountDownLatch isWatchClosed = new CountDownLatch(1);

        Watcher<StatefulSet> watcher = new Watcher<>() {
            @Override
            public void eventReceived(Action action, StatefulSet statefulSet) {
                switch (action.name()) {
                    case "ADDED":
                        logger.info("{} got added", statefulSet.getMetadata().getName());
                        break;
                    case "DELETED":
                        logger.info("{} got deleted", statefulSet.getMetadata().getName());
                        break;
                    case "MODIFIED":
                        logger.info("{} got modified", statefulSet.getMetadata().getName());
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
            client.apps().statefulSets().watch(watcher);
            // Wait till watch gets closed
            isWatchClosed.await();
        } catch (InterruptedException interruptedException) {
            logger.info( "Thread Interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}
