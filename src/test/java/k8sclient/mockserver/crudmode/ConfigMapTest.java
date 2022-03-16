package k8sclient.mockserver.crudmode;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.ConfigMapList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigMapTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(true, true);

    private final String NAMESPACE = "test";
    private final String ENV_VAR_DEPLOYMENT_NAME = "deploymentName";
    private KubernetesClient client;

    @Before
    public void before() {
        client = server.getClient().inNamespace(NAMESPACE);
    }

    @Test
    public void createCMTest() {
        String tldbYaml = "raftNodeCount: 1\n" +
                "backupScheduledHourGmt: 0\n" +
                "backupEverySomeHour: -1\n" +
                "backupRetentionCount: -1";
        ConfigMap configMap = new ConfigMapBuilder()
                .withNewMetadata()
                .addToLabels("foo", "bar")
                .withName("configmap")
                .endMetadata()
                .addToData("tldbYaml", tldbYaml)
                .build();

        client.configMaps().create(configMap);
        client.configMaps().list().getItems().forEach(cm -> System.out.println(cm));
    }

    @Test
    public void testCrud() {
        KubernetesClient client = server.getClient();

        ConfigMap configmap1 = new ConfigMapBuilder()
                .withNewMetadata()
                .withName("configmap1")
                .endMetadata()
                .addToData("one", "1")
                .build();
        ConfigMap configmap2 = new ConfigMapBuilder()
                .withNewMetadata()
                .addToLabels("foo", "bar")
                .withName("configmap2")
                .endMetadata()
                .addToData("two", "2")
                .build();
        ConfigMap configmap3 = new ConfigMapBuilder()
                .withNewMetadata()
                .addToLabels("foo", "bar")
                .withName("configmap2")
                .endMetadata()
                .addToData("three", "3")
                .build();

        client.configMaps().inNamespace("ns1").create(configmap1);
        client.configMaps().inNamespace("ns1").create(configmap2);
        client.configMaps().inNamespace("ns2").create(configmap3);

        ConfigMapList aConfigMapList = client.configMaps().list();
        assertNotNull(aConfigMapList);
        assertEquals(0, aConfigMapList.getItems().size());

        aConfigMapList = client.configMaps().inAnyNamespace().list();
        assertNotNull(aConfigMapList);
        assertEquals(3, aConfigMapList.getItems().size());

        aConfigMapList = client.configMaps().inAnyNamespace().withLabels(Collections.singletonMap("foo", "bar")).list();
        assertNotNull(aConfigMapList);
        assertEquals(2, aConfigMapList.getItems().size());

        aConfigMapList = client.configMaps().inNamespace("ns1").list();
        assertNotNull(aConfigMapList);
        assertEquals(2, aConfigMapList.getItems().size());

        // Delete
        client.configMaps().inNamespace("ns1").withName("configmap1").delete();
        aConfigMapList = client.configMaps().inNamespace("ns1").list();
        assertNotNull(aConfigMapList);
        assertEquals(1, aConfigMapList.getItems().size());

        // Update
        configmap2 = client.configMaps().inNamespace("ns1").withName("configmap2")
                .edit().addToData("II", "TWO").done();

        assertNotNull(configmap2);
        assertEquals("TWO", configmap2.getData().get("II"));
    }
}
