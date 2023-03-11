package io.fabric8;

import io.fabric8.kubernetes.api.model.Binding;
import io.fabric8.kubernetes.api.model.BindingBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class BindingDemo {
    public static void main(String[] args) {
        try (final KubernetesClient kubernetesClient = new KubernetesClientBuilder().build()) {
            Binding binding = new BindingBuilder()
                    .withNewMetadata().withName("exf6fv2").endMetadata()
                    .withNewTarget().withKind("Node").withApiVersion("v1").withName("minikube").endTarget()
                    .build();

            kubernetesClient.resource(binding).inNamespace("default").create();
        }
    }
}
