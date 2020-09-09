package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class AddingLabelToNamespace {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.namespaces().withName("default").edit()
                    .editMetadata()
                    .addToLabels("core.k8s/rbac-processed", String.valueOf(true))
                    .endMetadata().done();
        }
    }
}