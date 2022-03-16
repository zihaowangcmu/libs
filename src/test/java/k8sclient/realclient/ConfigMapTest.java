package k8sclient.realclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tracelink.dnp.utils.functional.Try;
import com.tracelink.dnp.utils.parser.Yaml;
import com.tracelink.wv.common.model.TlDbConfig;
import com.tracelink.wv.server.model.AwsConfigSchema;
import com.tracelink.wv.server.model.S3BucketConfiguration;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.vertx.core.json.Json;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ConfigMapTest {

    private KubernetesClient client = new DefaultKubernetesClient();

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void parseConfigMapTest() {
        // Remember to add the -conf suffix
        ConfigMap configMap = client.configMaps().withName("authdep-conf").get();
        String tldbYaml = configMap.getData().get("tldbYaml");

        Try<TlDbConfig> configs = Yaml.IMPL.decodeValueRelaxed(tldbYaml, TlDbConfig.class);
    }

    @Test
    public void getS3BucketNameTest() {
        // Remember to add the -conf suffix
        // this is the env manager cm
        ConfigMap configMap = client.configMaps().withName("96d22a0a-93e6-4355-a4f1-3817015204b0-conf").get();
        String appsYaml = configMap.getData().get("appsYaml");

        Try<String> bucketTry = Yaml.IMPL.decodeValueRelaxed(appsYaml, ObjectNode.class)
                .flatMap(node -> {
                    JsonNode firstAppNode = node.get("apps").get(0);
                    JsonNode firstManifestNode = null;
                    // Some ConfigMap do not contains the manifests, which are deployed earlier. Be backward compatible.
                    if (firstAppNode.hasNonNull("manifests")) {
                        firstManifestNode = firstAppNode.get("manifests").get(0);
                    } else {
                        firstManifestNode = firstAppNode;
                    }
                    String bucket = null;
                    AwsConfigSchema awsConfigSchema = Yaml.IMPL.decodeValueRelaxed(firstManifestNode.get("modules").get(0).get("aws").asText(), AwsConfigSchema.class).get();
                    System.out.println("awsConfigSchema: " + Json.encode(awsConfigSchema));
                    List<S3BucketConfiguration> configuredBuckets = awsConfigSchema.getS3Configuration().getConfiguredBuckets();
                    for (int i = 0; i < configuredBuckets.size(); i++) {
                        if (configuredBuckets.get(i).getBucketName().equals("dynamic")) {
                            bucket = configuredBuckets.get(i).getBucket();
                        }
                    }
                    System.out.println("S3 bucket for qualification info is: " + bucket);
                    if (bucket == null) {
                        return Try.failure(new NullPointerException("S3 bucket for qualification info is null"));
                    }
                    return Try.ofNullable(bucket);
                });

        String bucket = "";
        if (bucketTry.isFailure()) {
            // todo: fail the startup in the future. For now, ignore and log an error, then continue startup.
//            return Try.failure(bucketTry.getCause());
            System.out.println("Fail to get S3 bucket from ConfigMap. Will ignore and continue startup.");
        } else {
            bucket = bucketTry.get();
            System.out.println("Succeed! Bucket is: " + bucket);
        }
    }
}
