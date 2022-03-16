package k8sclient.realclient;

import io.fabric8.kubernetes.api.model.DeletionPropagation;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.TypedLocalObjectReference;
import io.fabric8.kubernetes.api.model.apps.DoneableStatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Follow this tutorial
 * https://kubernetes.io/docs/tutorials/stateful-application/basic-stateful-set/
 */
public class StatefulSetTest {

//    private final String DEFAULT_NAMESPACE = "default";
//    private final String DEFAULT_NAMESPACE = "devtest03";
//    private final String DEFAULT_NAMESPACE = "devtest04";
    private final String DEFAULT_NAMESPACE = "plattest03";
//    private final String DEFAULT_NAMESPACE = "plattest02";
    private KubernetesClient client = new DefaultKubernetesClient().inNamespace(DEFAULT_NAMESPACE);
    private final Logger logger = LoggerFactory.getLogger(StatefulSetTest.class);

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void listTest() {
        int size = client.apps().statefulSets().list().getItems().size();
        logger.info("statefulSets size: {}", size);

        StatefulSet qatldbdep = client.apps().statefulSets().withName("qatldbdep").get();
        System.out.println(qatldbdep);

        List<StatefulSet> items = client.apps().statefulSets().list().getItems();
        System.out.println(items.size());
    }

    @Test
    public void createTest() {
        // kubectl apply -f ./src/test/resources/k8s/statefulSet.yaml
        // https://kubernetes.io/docs/tutorials/stateful-application/basic-stateful-set/
        // https://kubernetes.io/docs/concepts/workloads/controllers/statefulset/#rolling-updates
    }

    @Test
    public void getStatefulSetPositive() {
        MixedOperation<StatefulSet, StatefulSetList, DoneableStatefulSet, RollableScalableResource<StatefulSet, DoneableStatefulSet>> ss =
                client.apps().statefulSets();
        StatefulSet statefulSet = ss.withName("web-with-specific-ss").get();

        // Labels
        logger.info("Labels: {}", statefulSet.getMetadata().getLabels());

        // Empty
        logger.info("Additional Properties: {}", statefulSet.getAdditionalProperties());

        // Get EnvVars
        for (EnvVar var : statefulSet.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv()) {
            if (var.getName().equals("DEPLOYMENT_NAME")) {
                logger.info("DEPLOYMENT_NAME: {}", var);
                break;
            }
        }

        // Get dataSource
        logger.info("DataSource: {}", statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec().getDataSource());

        // Get resourceVersion
        logger.info("ResourceVersion: {}", statefulSet.getMetadata().getResourceVersion());
    }

    @Test
    public void getStatefulSetNegative() {
        MixedOperation<StatefulSet, StatefulSetList, DoneableStatefulSet, RollableScalableResource<StatefulSet, DoneableStatefulSet>> ss =
                client.apps().statefulSets();
        StatefulSet statefulSet = ss.withName("fake").get();
        logger.info("statefulSet: {}", statefulSet);
    }

    @Test
    public void updateStorageClassTest() throws InterruptedException {
        String name = "tldbaccesscontrolnolinking";
        MixedOperation<StatefulSet, StatefulSetList, DoneableStatefulSet, RollableScalableResource<StatefulSet, DoneableStatefulSet>> ss =
                client.apps().statefulSets();

        // Get the ss
        StatefulSet statefulSet = ss.withName(name).get();
        logger.info("resourceVersion before deletion: {}", statefulSet.getMetadata().getResourceVersion());
        logger.info("dataSource before deletion: {}", statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec().getDataSource());

        // Delete ss. Use DeletionPropagation.ORPHAN to NOT delete dependents
        Boolean deleted = ss.withName(name).withPropagationPolicy(DeletionPropagation.ORPHAN).delete();
        logger.info("Deleted: {}", deleted);

        // Make sure ss is deleted
        int retries = 10;
        while (retries > 0 && ss.withName(name).get() != null) {
            Thread.sleep(1000);
            retries--;
        }
        if (retries == 0) {
            logger.info("Fail to delete ss");
        } else {
            logger.info("ss deleted!");
        }

        // change storage class
        statefulSet.getMetadata().setResourceVersion("");
        statefulSet.getMetadata().setName(name);
        statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec()
                .setStorageClassName("ebs-sc");

        // Deploy updated ss
        StatefulSet updatedStatefulSet = ss.withName(name).create(statefulSet);

        StatefulSet created = ss.withName(name).get();
        logger.info("resourceVersion after update: {}", created.getMetadata().getResourceVersion());
        logger.info("dataSource after update: {}", statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec().getDataSource());
    }

    /**
     * Restore/create a statefulSet with specific snapshot,
     * verify it is working by check the file: #/mnt/data/index.html exists.
     * Check:
     * k8sclient.realclient.VolumeSnapshotTest.java/createSnapshotTest()
     */
    @Test
    public void restoreDeploymentWithSpecificSnapshotTest() {
        // todo: if I need it, implement it correctly.
//        createStatefulSet("restoreTest", "web-0-ss");
    }

    public void createStatefulSet(String statefulSetName, String volumeSnapshotName) {
        logger.info("Creating statefulSet: {} with snapshot: {}", statefulSetName, volumeSnapshotName);

        StatefulSet ss1 = new StatefulSetBuilder()
                .withNewMetadata()
                .withName(statefulSetName)
                .withNamespace(DEFAULT_NAMESPACE)
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
                // mock env vars
//                .addNewEnv()
//                .withName(ENV_VAR_DEPLOYMENT_NAME)
//                .withValue(deploymentName)
//                .endEnv()
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
        logger.info("statefulSet: {} created",
                client.apps().statefulSets().withName(statefulSetName).get().getMetadata().getName());
    }



    // ************************* For Migration *************************

    @Test
    public void migrationDeleteAndCreateTest() throws InterruptedException {
        String statefulSet = "experiencemanagerjsdep";
        deleteAndCreateTest(statefulSet);
    }

//    @Test
    public void deleteAndCreateTest(String name) throws InterruptedException {
        MixedOperation<StatefulSet, StatefulSetList, DoneableStatefulSet, RollableScalableResource<StatefulSet, DoneableStatefulSet>> ss =
                client.apps().statefulSets();

        // Get the ss
        StatefulSet statefulSet = ss.withName(name).get();
        logger.info("resourceVersion before deletion: {}", statefulSet.getMetadata().getResourceVersion());
        logger.info("dataSource before deletion: {}", statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec().getDataSource());

        // Delete ss. Use DeletionPropagation.ORPHAN to NOT delete dependents
        Boolean deleted = ss.withName(name).withPropagationPolicy(DeletionPropagation.ORPHAN).delete();
        logger.info("Deleted: {}", deleted);

        // Make sure ss is deleted
        int retries = 10;
        while (retries > 0 && ss.withName(name).get() != null) {
            Thread.sleep(1000);
            retries--;
        }
        if (retries == 0) {
            logger.info("Fail to delete ss");
        } else {
            logger.info("ss deleted!");
        }

        // Edit ss
        statefulSet.getMetadata().setResourceVersion("");
        statefulSet.getMetadata().setName(name);
        TypedLocalObjectReference dataSource = new TypedLocalObjectReference();
        String snapshotName = name + "-1-migration";
        dataSource.setName(snapshotName);
        dataSource.setKind("VolumeSnapshot");
        dataSource.setApiGroup("snapshot.storage.k8s.io");

        statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec()
                .setDataSource(dataSource);

////         Remove snapshot
//        statefulSet.getMetadata().setResourceVersion("");
//        statefulSet.getMetadata().setName(name);
//        statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec()
//                .setDataSource(null);

//        Map<String, String> labels = new HashMap<>();
//        labels.put("test", "2");
//        statefulSet.getMetadata().setLabels(labels);

        // Deploy updated ss
        StatefulSet updatedStatefulSet = ss.withName(name).create(statefulSet);

        StatefulSet created = ss.withName(name).get();
        logger.info("resourceVersion after update: {}", created.getMetadata().getResourceVersion());
        logger.info("dataSource after update: {}", statefulSet.getSpec().getVolumeClaimTemplates().get(0).getSpec().getDataSource());
    }
}
