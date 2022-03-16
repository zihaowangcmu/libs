package k8sclient.mockserver.noncrudmode;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshot;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshotBuilder;
import io.fabric8.volumesnapshot.client.VolumeSnapshotClient;
import io.fabric8.volumesnapshot.server.mock.VolumeSnapshotServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NonCurdVolumeServerTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(true, true);

    @Rule
    public VolumeSnapshotServer volumeSnapshotServer = new VolumeSnapshotServer(true, true);

    private KubernetesClient kubeClient;
    private VolumeSnapshotClient volumeSnapshotClient;
    private final String NAMESPACE = "test";


    @Before
    public void before() {
        // Mock kubeClient, use namespace: "test"
        kubeClient = server.getClient().inNamespace(NAMESPACE);
        // Mock VolumeSnapshotServer, use namespace: "test"
        volumeSnapshotClient = volumeSnapshotServer.get();
    }

    @Test
    public void createVolumeSnapshotTest() {
        String snapshotName = "createVolumeSnapshotTest";
        String volumeSnapshotClassName = "testVSC";
        String pvcName = "pvc";

        VolumeSnapshot volumeSnapshot = volumeSnapshotClient.volumeSnapshots()
                .create(new VolumeSnapshotBuilder()
                        .withNewMetadata()
                        .withName(snapshotName)
                        .endMetadata()
                        .withNewSpec()
                        .withNewVolumeSnapshotClassName(volumeSnapshotClassName)
                        .withNewSource()
                        .withPersistentVolumeClaimName(pvcName)
                        .endSource()
                        .endSpec()
                        .build());
    }
}
