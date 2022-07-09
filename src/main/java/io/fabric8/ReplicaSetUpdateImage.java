package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ReplicaSetUpdateImage {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.apps().replicaSets()
                    .inNamespace("default")
                    .withName("soaktestrs")
                    .rolling()
                    .updateImage("nickchase/soaktest");
        }
    }
}
