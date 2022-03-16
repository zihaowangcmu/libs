package k8sclient.realclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.NodeSelectorRequirementBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolume;
import io.fabric8.kubernetes.api.model.PersistentVolumeBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpecBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.TypedLocalObjectReferenceBuilder;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import io.fabric8.kubernetes.client.internal.SerializationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

public class PVCTest {

    private KubernetesClient client = new DefaultKubernetesClient();

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void getPVCByName() throws JsonProcessingException {
//        String pvcName = "tlqetldbperf-tlqetldbperf-0";
        String pvcName = "jstldbperf-jstldbperf-0";
        PersistentVolumeClaim pvc = client.persistentVolumeClaims().withName(pvcName).get();

        String pvcAsYaml = SerializationUtils.dumpAsYaml(pvc);
        // Your pod might have some state that you don't really care about, to remove it:
        String pvcAsYamlWithoutRuntimeState = SerializationUtils.dumpWithoutRuntimeStateAsYaml(pvc);
        System.out.println("======================");
        System.out.println(pvcAsYaml);
//        System.out.println("======================");
//        System.out.println(pvcAsYamlWithoutRuntimeState);

//        pvc.getSpec().getResources().getRequests().getOrDefault("storage", -1);
//        Quantity storage = pvc.getSpec().getResources().getRequests().get("storage");
        Quantity storage = pvc.getSpec().getResources().getRequests().get("null");
//        storage
    }


    /**
     * Do NOT use this way to get PVC info. Only get the PVC name of the pod.
     * Use getPVCByName() to get PVC.
     */
    @Test
    public void getPVCForPod() {
//        String podName = "tlqetldbperf-0";
        String podName = "jstldbperf-0";
        Pod pod = client.pods().withName(podName).get();
//        System.out.println(pod.getSpec().getVolumes());
        System.out.println(pod.getSpec().getVolumes().size());
        for (Volume v : pod.getSpec().getVolumes()) {
            if (v != null && v.getPersistentVolumeClaim() != null) {
//                String pvcAsYaml = SerializationUtils.dumpAsYaml(v.getPersistentVolumeClaim());
                System.out.println(v.getPersistentVolumeClaim().getClaimName());
                System.out.println(v.getPersistentVolumeClaim());
            }
        }
    }
}
