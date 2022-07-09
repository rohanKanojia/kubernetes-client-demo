package io.fabric8;

import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class TopExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            NodeMetricsList nodeMetricsList = client.top().nodes().metrics();
            System.out.println(nodeMetricsList.getItems().size() + " items found.");
        }
    }
}