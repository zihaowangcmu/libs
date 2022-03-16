package k8sclient.realclient.watcher;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class WatcherPodTest {

    private static final Logger logger = LoggerFactory.getLogger(WatcherPodTest.class.getSimpleName());
    private static String namespace = "default";

    public static void main(String[] args) {

        // Latch for Watch termination
        final CountDownLatch isWatchClosed = new CountDownLatch(1);
        try (final KubernetesClient k8s = new DefaultKubernetesClient()) {
            k8s.pods().inNamespace(namespace).watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {
                    logger.info("{} {}", action.name(), pod.getMetadata().getName());
                    switch (action.name()) {
                        case "ADDED":
                            logger.info("{} got added", pod.getMetadata().getName());
                            break;
                        case "DELETED":
                            logger.info("{} got deleted", pod.getMetadata().getName());
                            break;
                        case "MODIFIED":
                            logger.info("{} got modified",  pod.getMetadata().getName());
                            break;
                        default:
                            logger.error("Unrecognized event: {}", action.name());
                    }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                    logger.info( "Closed");
                    isWatchClosed.countDown();
                }
            });

            // Wait till watch gets closed
            isWatchClosed.await();
        } catch (InterruptedException interruptedException) {
            logger.info( "Thread Interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}