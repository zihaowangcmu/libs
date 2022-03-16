package k8sclient.mockserver.crudmode;

import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshot;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshotBuilder;
import io.fabric8.volumesnapshot.client.DefaultVolumeSnapshotClient;
import io.fabric8.volumesnapshot.client.VolumeSnapshotClient;
import io.fabric8.volumesnapshot.server.mock.VolumeSnapshotServer;
import k8sclient.realclient.watcher.WatcherPodTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VolumeSnapshotTest {

    private final String NAMESPACE = "test";

    @Rule
    public KubernetesServer server = new KubernetesServer(true, true);

    @Rule
    public VolumeSnapshotServer volumeSnapshotServer = new VolumeSnapshotServer(true, true);

    private KubernetesClient client;
    private VolumeSnapshotClient volumeSnapshotClient;
    private static final Logger logger = LoggerFactory.getLogger(VolumeSnapshotTest.class.getSimpleName());

    @Before
    public void before() {
        // Mock kubeClient, use namespace: "test"
        client = server.getClient().inNamespace(NAMESPACE);
        // Mock VolumeSnapshotServer, use namespace: "test"
        volumeSnapshotClient = volumeSnapshotServer.get();
    }

    @After
    public void after() {}

    /**
     * Test for list options
     * ======================== IMPORTANT ========================
     * the listOptions is not working with the test client
     * Use the real client to test!!!
     * ===========================================================
     */
    @Test
    public void listOptionsTest() {
        logger.info("Creating a volume snapshot");

        String volumeSnapshotClassName = "testVSC";
        String pvcName = "pvc";
        List<String> names = new ArrayList(List.of("ss1", "ss2", "ss3", "ss4"));
        names.forEach(name -> {
            volumeSnapshotClient.volumeSnapshots()
                    .create(new VolumeSnapshotBuilder()
                            .withNewMetadata()
                            .withName(name)
                            .endMetadata()
                            .withNewSpec()
                            .withNewVolumeSnapshotClassName(volumeSnapshotClassName)
                            .withNewSource()
                            .withPersistentVolumeClaimName(pvcName)
                            .endSource()
                            .endSpec()
                            .build());
            logger.info("done {}", name);
        });

        assertEquals(names.size(), volumeSnapshotClient.volumeSnapshots().list().getItems().size());

        /*
         * Note:
         *
         * known issue: the listOptions is not working with the test client
         * Use the real client to test!!!
         */
        ListOptions listOptions = new io.fabric8.kubernetes.api.model.ListOptionsBuilder()
                .withLimit(2L)
                .withContinue(null)
//                .withFieldSelector("metadata.name=my-service")
                .build();

        logger.info("size with limit 2: {}",
                volumeSnapshotClient.volumeSnapshots().list(listOptions).getItems().size());
    }

    /**
     * Edit volumesnapshot tag
     */
    @Test
    public void editTagTest() {
        logger.info("Creating a volume snapshot");

        String name = "ss1";
        String volumeSnapshotClassName = "testVSC";
        String pvcName = "pvc";
        volumeSnapshotClient.volumeSnapshots()
                .create(new VolumeSnapshotBuilder()
                        .withNewMetadata()
                        .withName(name)
                        .endMetadata()
                        .withNewSpec()
                        .withNewVolumeSnapshotClassName(volumeSnapshotClassName)
                        .withNewSource()
                        .withPersistentVolumeClaimName(pvcName)
                        .endSource()
                        .endSpec()
                        .build());

        assertNull(volumeSnapshotClient.volumeSnapshots().withName(name).get().getMetadata().getLabels());

        volumeSnapshotClient.volumeSnapshots().withName(name).edit()
                .editMetadata()
                .addToLabels("newKey", "newValue")
                .addToLabels("negative", "-1")
                .endMetadata()
                .done();

        assertEquals(volumeSnapshotClient.volumeSnapshots().withName(name).get().getMetadata().getLabels().size(), 1);
    }
}
