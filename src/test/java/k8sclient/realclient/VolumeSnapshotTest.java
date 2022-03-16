package k8sclient.realclient;

import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshot;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshotBuilder;
import io.fabric8.volumesnapshot.api.model.VolumeSnapshotList;
import io.fabric8.volumesnapshot.client.DefaultVolumeSnapshotClient;
import io.fabric8.volumesnapshot.client.VolumeSnapshotClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class VolumeSnapshotTest {

//    private final String DEFAULT_NS = "default";
//    private final String DEFAULT_NS = "devtest03";
    private final String DEFAULT_NS = "plattest02";
    private KubernetesClient client = new DefaultKubernetesClient().inNamespace(DEFAULT_NS);
    private VolumeSnapshotClient volumeSnapshotClient = new DefaultVolumeSnapshotClient().inNamespace(DEFAULT_NS);
    private static final Logger logger = LoggerFactory.getLogger(VolumeSnapshotTest.class.getSimpleName());

    @Before
    public void before() {}

    @After
    public void after() {}

    /**
     * Create VolumeSnapshot test
     * Before it, make sure you have different content in the default snapshot and the target snapshot,
     * so that after restore/scale up we can see the difference.
     * A simple way is to save a file on a pod, and take snapshot for it. Take a snapshot for it, then
     * restore with the snapshot and verify the file exists.
     * For a tutorial, see:
     * https://kubernetes.io/docs/tasks/configure-pod-container/configure-persistent-volume-storage/
     */
    @Test
    public void createSnapshotTest() {
        System.out.println("Creating a volume snapshot");

        // pod web-0 has PVC www-web-0, and it has a file: #/mnt/data/index.html
        // take a ss for www-web-0
        volumeSnapshotClient.volumeSnapshots()
                .create(new VolumeSnapshotBuilder()
                        .withNewMetadata()
                        .withName("web-0-ss")
                        .endMetadata()
                        .withNewSpec()
                        .withNewVolumeSnapshotClassName("zihao")
                        .withNewSource()
                        .withPersistentVolumeClaimName("www-web-0")
                        .endSource()
                        .endSpec()
                        .build());

        // pod web-1 has PVC www-web-1, and it does not have the file
        // take a ss for www-web-1
        volumeSnapshotClient.volumeSnapshots()
                .create(new VolumeSnapshotBuilder()
                        .withNewMetadata()
                        .withName("web-1-ss")
                        .endMetadata()
                        .withNewSpec()
                        .withNewVolumeSnapshotClassName("zihao")
                        .withNewSource()
                        .withPersistentVolumeClaimName("www-web-1")
                        .endSource()
                        .endSpec()
                        .build());
    }

    /**
     * Test to list with labels
     */
    @Test
    public void listWithLabelsTest() {
        logger.info("Creating a volume snapshot");

        String volumeSnapshotClassName = "zihao";
        String pvcName = "zihao-restore";
        List<String> names = new ArrayList(List.of("ss1", "ss2", "ss3", "ss4"));
        List<Integer> ints = new ArrayList(List.of(1, 2, 3, 4));
        List<Map<String, String>> labels = generateLabels(names, ints);
        List<VolumeSnapshot> volumeSnapshots = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            volumeSnapshots.add(volumeSnapshotClient.volumeSnapshots()
                    .create(new VolumeSnapshotBuilder()
                            .withNewMetadata()
                            .withName(names.get(i))
                            .withLabels(labels.get(i))
                            .endMetadata()
                            .withNewSpec()
                            .withNewVolumeSnapshotClassName(volumeSnapshotClassName)
                            .withNewSource()
                            .withPersistentVolumeClaimName(pvcName)
                            .endSource()
                            .endSpec()
                            .build()));
            logger.info("created snapshot {}", names.get(i));
        };

        int allVolumeSnapshots = volumeSnapshotClient.volumeSnapshots().list().getItems().size();
        logger.info("allVolumeSnapshots size: {}", allVolumeSnapshots);

        int snapshotsTestSize1 = volumeSnapshotClient.volumeSnapshots()
                .withLabel("isTest")
                .list()
                .getItems()
                .size();

        logger.info("snapshots size having label: 'isTest' is : {}", snapshotsTestSize1);

        int snapshotsTestSize2 = volumeSnapshotClient.volumeSnapshots()
                .withLabel("name", "ss1")
                .list()
                .getItems()
                .size();

        logger.info("snapshots size with label: 'name' being 'ss1' is : {}", snapshotsTestSize2);

        volumeSnapshotClient.volumeSnapshots().delete(volumeSnapshots);
    }

    /**
     * Use limit + pagination to define the query.
     * Get the pagination from last query by:
     *  volumeSnapshotClient.volumeSnapshots().list(listOptions).getMetadata().getContinue()
     *
     * If the getContinue() returns "", that means it is the end of the query(all matches are returned, last page).
     * If you use
     *  .withContinue(null); OR
     *  withContinue("")
     * it will query from page 0.
     */
    @Test
    public void listWithLimitAndContinueTest() {
        logger.info("Creating a volume snapshot");
        long limit = 4;

        int allVolumeSnapshots = volumeSnapshotClient.volumeSnapshots().list().getItems().size();
        logger.info("allVolumeSnapshots size: {}", allVolumeSnapshots);

        String _continue = null;
        boolean end = false;
        int round = 0;
        while (!end) {
            ListOptions listOptions = new io.fabric8.kubernetes.api.model.ListOptionsBuilder()
                    .withLimit(limit)
                    .withContinue(_continue)
                    .build();
            VolumeSnapshotList list = volumeSnapshotClient.volumeSnapshots()
                    .list(listOptions);

            int snapshotsSizeOneWithPagination = list.getItems().size();
            _continue = list.getMetadata().getContinue();
            logger.info("current round: {}, snapshots size: {}, pagination: {}", round++,
                    snapshotsSizeOneWithPagination, _continue);
            end = _continue == "";
        }

        logger.info("Finish query all snapshots");
    }

    private List<Map<String, String>> generateLabels(List<String> names, List<Integer> ints) {
        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("class", this.getClass().getSimpleName());
            map.put("isTest", "true");
            map.put("name", names.get(i));
            map.put("intValue", ints.get(i) + "");
            res.add(map);
        }
        return res;
    }


    // ************************* For Migration *************************
    @Test
    public void migrationCreateSnapshotTest() {
        String statefulSet = "prod-base-environmentmanager";
        createTest(statefulSet);
    }


//    @Test
    public void createTest(String statefulSet) {
        System.out.println("Creating a volume snapshot");

        // pod web-0 has PVC www-web-0, and it has a file: #/mnt/data/index.html
        // take a ss for www-web-0
        volumeSnapshotClient.volumeSnapshots()
                .create(new VolumeSnapshotBuilder()
                        .withNewMetadata()
                        .withName(statefulSet + "-1-migration")
                        .endMetadata()
                        .withNewSpec()
                        .withNewVolumeSnapshotClassName("csi-snapclass")
                        .withNewSource()
                        .withPersistentVolumeClaimName(statefulSet + "-" + statefulSet + "-" + "1")
                        .endSource()
                        .endSpec()
                        .build());
    }
}
