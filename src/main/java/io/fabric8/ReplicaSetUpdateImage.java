package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ReplicaSetUpdateImage {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.apps().replicaSets()
                    .inNamespace("default")
                    .withName("soaktestrs")
                    .rolling()
                    .updateImage("nickchase/soaktest");
        }
    }
}
