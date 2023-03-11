package io.fabric8.demos;

import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class BookCustomResourceDefinitionCreate {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            CustomResourceDefinition crd = client.apiextensions().v1().customResourceDefinitions()
                    .load(BookCustomResourceDefinitionCreate.class.getResourceAsStream("/book-crd.yaml"))
                    .get();
            client.apiextensions().v1().customResourceDefinitions().resource(crd).create();
        }
    }
}
