package io.fabric8;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public class ListPodsSortedCreationTimestamp {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            List<Pod> podList = client.pods().inNamespace("default").list().getItems();
            podList.sort((Comparator<HasMetadata>) (o1, o2) -> {
                int o1Timestamp = (int) Instant.parse(o1.getMetadata().getCreationTimestamp()).getEpochSecond();
                int o2Timestamp = (int) Instant.parse(o2.getMetadata().getCreationTimestamp()).getEpochSecond();
                return o1Timestamp - o2Timestamp;
            });
            podList.forEach(p -> System.out.println(p.getMetadata().getName() + " " + p.getMetadata().getCreationTimestamp()));
        }
    }
}