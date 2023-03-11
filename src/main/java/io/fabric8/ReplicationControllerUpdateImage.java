package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ReplicationControllerUpdateImage {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.replicationControllers()
                    .inNamespace("default")
                    .withName("nginx")
                    .updateImage("nginx:latest");
        }
    }
}
