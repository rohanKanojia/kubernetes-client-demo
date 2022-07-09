package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.HashMap;
import java.util.Map;

public class DeploymentRollingUpdateImage {
    public static void main(String[] args) {
try (KubernetesClient client = new KubernetesClientBuilder().build()) {
    String namespace = "default";

    Deployment deployment = client.apps().deployments()
            .inNamespace(namespace)
            .withName("hello-dep")
            .get();

    client.apps().deployments()
            .inNamespace(namespace)
            .withName(deployment.getMetadata().getName())
            .rolling()
            .updateImage("gcr.io/google-samples/hello-app:2.0");

//    rollingUpdateMultiContainerDeployment(client, namespace);
    client.apps().deployments()
            .inNamespace(namespace)
            .withName(deployment.getMetadata().getName())
            .rolling()
            .restart();
}
    }

    private static void rollingUpdateMultiContainerDeployment(KubernetesClient client, String namespace) {
        Map<String, String> containerToImageMap = new HashMap<>();
        containerToImageMap.put("nginx", "stable-perl");
        containerToImageMap.put("hello", "hello-world:linux");
        client.apps().deployments()
                .inNamespace(namespace)
                .withName("multi-container-deploy")
                .rolling()
                .updateImage(containerToImageMap);
    }

}
