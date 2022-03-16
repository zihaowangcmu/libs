package k8sclient.mockserver.crudmode;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

public class StatefulSetTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(true, true);

    private final String NAMESPACE = "test";
    private final String ENV_VAR_DEPLOYMENT_NAME = "deploymentName";
    private KubernetesClient client;

    @Before
    public void before() {
        client = server.getClient().inNamespace(NAMESPACE);
    }

    // Note the mock client will not generate pod for ss
    @Test
    public void createSSTest() {
        String statefulSetName = "ss1";
        String deploymentName = "dep1";
        StatefulSet ss1 = new StatefulSetBuilder()
                .withNewMetadata()
                .withName(statefulSetName)
                .withNamespace(NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .addNewVolumeClaimTemplate()
                .withNewSpec()
                // add dataSource here
                .endSpec()
                .endVolumeClaimTemplate()
                .withReplicas(1)
                .withNewTemplate()
                .withNewMetadata().withLabels(new HashMap<String, String>()).endMetadata()
                .withNewSpec()
                // app container
                .addNewContainer()
                .withName("app")
                .withNewImage("kernel-img")
                .addNewEnv()
                .withName(ENV_VAR_DEPLOYMENT_NAME)
                .withValue(deploymentName)
                .endEnv()
                .endContainer()
                // worldview container
                .addNewContainer()
                .withName("worldview")
                .withNewImage("tldb-image")
                .endContainer()
                .endSpec()
                .endTemplate()
                .endSpec()
                .withNewStatus().withReplicas(1).endStatus()
                .build();


        client.apps().statefulSets().create(ss1);
        System.out.println(client.apps().statefulSets().withName(statefulSetName).get());
    }
}
