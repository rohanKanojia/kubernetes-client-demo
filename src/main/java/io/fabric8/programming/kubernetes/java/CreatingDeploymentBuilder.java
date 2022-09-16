package io.fabric8.programming.kubernetes.java;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CreatingDeploymentBuilder {
  public static void main(String[] args) {
    Deployment deployment = new DeploymentBuilder()
            .withNewMetadata()
                .withName("nginx-deployment")
                .addToLabels("app", "nginx")
            .endMetadata()
            .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                    .addToMatchLabels("app", "nginx")
                .endSelector()
            .withNewTemplate()
                .withNewMetadata()
                    .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                    .addNewContainer()
                        .withName("nginx")
                        .withImage("nginx:1.7.9")
                        .addNewPort()
                            .withContainerPort(80)
                        .endPort()
                    .endContainer()
                .endSpec()
            .endTemplate()
            .endSpec()
        .build();

    try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().build()) {
      kubernetesClient.apps().deployments()
          .inNamespace("default")
          .resource(deployment)
          .create();
    }
  }
}
