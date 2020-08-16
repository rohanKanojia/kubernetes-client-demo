package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Collections;

public class ExtensionsV1Beta1DeploymentCreateOrReplace {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Deployment deployment = new DeploymentBuilder()
                    .withNewMetadata().withName("nginx-deployment").addToLabels("app", "nginx").endMetadata()
                    .withNewSpec()
                    .withReplicas(3)
                    .withNewSelector()
                    .withMatchLabels(Collections.singletonMap("app", "nginx"))
                    .endSelector()
                    .withNewTemplate()
                    .withNewMetadata().addToLabels("app", "nginx").endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("nginx")
                    .withImage("nginx:1.7.9")
                    .addNewPort().withContainerPort(80).endPort()
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();
            client.extensions().deployments().inNamespace("default").createOrReplace(deployment);
        }
    }
}