package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class CustomResourceDefinitionListExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.apiextensions().v1()
                    .customResourceDefinitions()
                    .list()
                    .getItems().forEach(crd -> System.out.println(crd.getMetadata().getName()));
        }
    }
}