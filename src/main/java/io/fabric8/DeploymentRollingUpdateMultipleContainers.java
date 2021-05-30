package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.HashMap;
import java.util.Map;

public class DeploymentRollingUpdateMultipleContainers {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Map<String, String> containerToImageMap = new HashMap<>();
            containerToImageMap.put("nginx", "stable-perl");
            containerToImageMap.put("hello", "hello-world:linux");
            client.apps().deployments()
                    .inNamespace("default")
                    .withName("multi-container-deploy")
                    .rolling()
                    .updateImage(containerToImageMap);
        }
    }
}
