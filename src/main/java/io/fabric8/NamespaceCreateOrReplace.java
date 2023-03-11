package io.fabric8;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class NamespaceCreateOrReplace {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            Namespace namespace = new NamespaceBuilder()
                    .withNewMetadata().withName("this-is-a-test").endMetadata()
                    .build();

            client.namespaces().resource(namespace).serverSideApply();
            System.out.println("Done");
        }
    }
}
