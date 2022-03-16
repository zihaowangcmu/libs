package k8sclient.realclient;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.volumesnapshot.client.DefaultVolumeSnapshotClient;
import io.fabric8.volumesnapshot.client.VolumeSnapshotClient;

public class utils {

    public static VolumeSnapshotClient newClient(String[] args) {
        ConfigBuilder config = new ConfigBuilder();
        for (int i = 0; i < args.length - 1; i++) {

            String key = args[i];
            String value = args[i + 1];

            if (key.equals("--api-server")) {
                config = config.withMasterUrl(value);
            }

            if (key.equals("--token")) {
                config = config.withOauthToken(value);
            }

            if (key.equals("--username")) {
                config = config.withUsername(value);
            }

            if (key.equals("--password")) {
                config = config.withPassword(value);
            }
            if (key.equals("--namespace")) {
                config = config.withNamespace(value);
            }
        }
        return new DefaultVolumeSnapshotClient(config.build());
    }
}
