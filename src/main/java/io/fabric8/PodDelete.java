package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class PodDelete {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.pods().inNamespace("default").withName("test-pod1").delete();
        }
    }
}