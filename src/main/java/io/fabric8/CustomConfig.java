package io.fabric8;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class CustomConfig {
    public static void main(String[] args) {
        int max = 512;
        System.setProperty("kubernetes.disable.autoConfig", "true");
        Config config = new ConfigBuilder(Config.autoConfigure(null))
          .withMaxConcurrentRequests(max    )
                .build();
        try (KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build()) {
            client.pods().list();
        }
    }
}
