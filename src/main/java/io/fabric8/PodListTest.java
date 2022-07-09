package io.fabric8;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class PodListTest {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            PodList podList = client.pods().inAnyNamespace().list();
            System.out.println("Found " + podList.getItems().size() + " pods.");
        }
    }
}
