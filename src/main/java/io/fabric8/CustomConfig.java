package io.fabric8;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class CustomConfig {
    public static void main(String[] args) {
        System.setProperty("kubernetes.disable.autoConfig", "true");
        Config config = new ConfigBuilder()
                .build();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            client.pods().list();
        }
    }
}
