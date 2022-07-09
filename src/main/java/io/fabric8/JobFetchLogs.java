package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class JobFetchLogs {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            String log = client.batch().v1().jobs().inNamespace("default").withName("pi").getLog();
            System.out.println(log);
        }
    }
}