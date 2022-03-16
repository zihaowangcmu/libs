package k8sclient.realclient;

import io.fabric8.volumesnapshot.api.model.VolumeSnapshotClass;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshotClassBuilder;
import io.fabric8.volumesnapshot.client.DefaultVolumeSnapshotClient;
import io.fabric8.volumesnapshot.client.VolumeSnapshotClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static k8sclient.realclient.utils.newClient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VolumeSnapshotClassTest {

    private String KUBE_SYSTEM = "kube_system";
    private VolumeSnapshotClient client = new DefaultVolumeSnapshotClient();

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void CRUDTest() throws InterruptedException {
        System.out.println(client.getNamespace());

        String name = "zihao";
        VolumeSnapshotClass vsc1 = new VolumeSnapshotClassBuilder()
                .withNewMetadata()
                .withName(name)
                // addLabels take String as value
                .addToLabels("key1", "value1")
                .endMetadata()
                .withDriver("hostpath.csi.k8s.io")
                .withDeletionPolicy("Delete")
                .build();
        client.volumeSnapshotClasses().create(vsc1);

//        //Read
//        VolumeSnapshotClassList vscList = client.volumeSnapshotClasses().list();
//        assertNotNull(vscList);
//        // another is csi-hostpath-snapclass in minikube
//        assertEquals(2, vscList.getItems().size());
//
//        VolumeSnapshotClassResource scr1 = client.volumeSnapshotClasses().withName(name);
//        VolumeSnapshotClass sc1 = scr1.get();
//        assertNotNull(sc1);
//
//        //Update
//        VolumeSnapshotClass u1 = client.volumeSnapshotClasses().withName(name).edit(v -> new VolumeSnapshotClassBuilder(v)
//                .editMetadata()
//                .addToLabels("updated", "true")
//                .endMetadata()
//                .build());
//
//        assertNotNull(u1);
//        assertEquals("true", u1.getMetadata().getLabels().get("updated"));
//
//        //Delete
//        assertTrue(scr1.delete());
//        // Only sleep for test
//        Thread.sleep(1000);
//        assertNull(scr1.get());
    }

}
