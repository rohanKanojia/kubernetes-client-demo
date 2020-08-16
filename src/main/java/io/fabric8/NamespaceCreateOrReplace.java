package io.fabric8;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class NamespaceCreateOrReplace {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Namespace namespace = new NamespaceBuilder()
                    .withNewMetadata().withName("this-is-a-test").endMetadata()
                    .build();

            client.namespaces().createOrReplace(namespace);
        }
    }
}
