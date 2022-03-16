package k8sclient.mockserver.noncrudmode;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.api.model.apps.StatefulSetListBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NonCrudKubeServerTest {

    @Rule
    public KubernetesServer server = new KubernetesServer();

    @Test
    public void createSS() {
        KubernetesClient client = server.getClient();
        server.expect().withPath("/apis/apps/v1/namespaces/test/statefulsets").andReturn(200, new StatefulSetListBuilder().build()).once();
        server.expect().withPath("/apis/apps/v1/namespaces/ns1/statefulsets").andReturn(200,  new StatefulSetListBuilder()
                .addNewItem().and()
                .addNewItem().and().build())
                .once();

        StatefulSetList statefulSetList = client.apps().statefulSets().list();
        assertNotNull(statefulSetList);
        assertEquals(0, statefulSetList.getItems().size());

        statefulSetList = client.apps().statefulSets().inNamespace("ns1").list();
        assertNotNull(statefulSetList);
        assertEquals(2, statefulSetList.getItems().size());
    }
}
