package k8sclient.mockserver.crudmode;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PodTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(true, true);

    private final String NAMESPACE = "test";
    private KubernetesClient client;

    @Before
    public void before() {
        client = server.getClient().inNamespace(NAMESPACE);
    }

    @Test
    public void getPodTest() {
        String podName = "pod1";
        Pod pod1 = new PodBuilder().withNewMetadata().withName(podName)
                .addToLabels("testKey", "testValue").endMetadata().build();
        client.pods().create(pod1);

        System.out.println(client.pods().withName(podName).get());
    }
}
