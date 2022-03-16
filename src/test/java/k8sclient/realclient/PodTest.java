package k8sclient.realclient;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PodTest {

    private KubernetesClient client = new DefaultKubernetesClient();

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void listPods() {
        // List Pods
        PodList list = client.pods().list();
        Pod pod = list.getItems().get(0);
        System.out.println(pod.getApiVersion());
        System.out.println(pod.getKind());
        System.out.println(pod.getMetadata().getName());
    }

    @Test
    public void getPod() {
        Pod pod = client.pods().withName("tlqetldbperf-0").get();
        System.out.println(pod.getMetadata().getName());
        System.out.println(pod.getMetadata().getCreationTimestamp());
    }
}
