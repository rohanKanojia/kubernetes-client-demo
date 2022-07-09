package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class DeploymentRollingUpdateUndo {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.apps().deployments()
                    .inNamespace("default")
                    .withName("hello-dep")
                    .rolling()
                    .undo();
        }
    }
}
