package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class PodDelete {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.pods().inNamespace("default").withName("test-pod1").delete();
        }
    }
}