package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ReplicationControllerUpdateImage {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.replicationControllers()
                    .inNamespace("default")
                    .withName("nginx")
                    .rolling()
                    .updateImage("nginx:latest");
        }
    }
}
