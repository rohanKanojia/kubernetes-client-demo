package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.HashMap;
import java.util.Map;

public class UpdateMultipleImagesDeployment {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            String namespace = "default";
            Map<String, String> updatedImages = new HashMap<>();
            updatedImages.put("busybox", "busybox:1");
            updatedImages.put("hello-kubernetes", "paulbouwer/hello-kubernetes:1.8");

            client.apps().deployments().inNamespace(namespace).withName("hello-kubernetes")
                    .rolling().updateImage(updatedImages);
        }
    }
}