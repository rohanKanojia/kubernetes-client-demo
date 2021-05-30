package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class DeploymentRollingUpdateImageExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.apps().deployments()
                    .inNamespace("default")
                    .withName("hello-dep")
                    .rolling()
                    .updateImage("gcr.io/google-samples/hello-app:2.0");
        }
    }
}
