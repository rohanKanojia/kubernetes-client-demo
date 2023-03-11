package io.fabric8;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class DeploymentAndServiceCreate {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Deployment
            String namespace = "default";
            Deployment deployment = createNewDeployment("hello-dep");
            client.apps().deployments().inNamespace(namespace).resource(deployment).serverSideApply();

            // Create Service
            Service service = createNewService("hello-dep");
            client.services().inNamespace(namespace).resource(service).serverSideApply();

        }
    }

    private static Service createNewService(String name) {
        return new ServiceBuilder()
                     .withNewMetadata()
                         .withName(name)
                     .endMetadata()
                     .withNewSpec()
                         .addNewPort()
                             .withName("http")
                             .withPort(8080)
                             .withProtocol("TCP")
                             .withTargetPort(new IntOrString(8080))
                         .endPort()
                         .addToSelector("app", "hello-dep")
                         .withType("NodePort")
                     .endSpec()
                .build();
    }

    private static Deployment createNewDeployment(String name) {
        return new DeploymentBuilder()
                .withNewMetadata()
                    .withName(name)
                .endMetadata()
                .withNewSpec()
                    .withReplicas(2)
                    .withNewSelector()
                        .addToMatchLabels("app", "hello-dep")
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToLabels("app", "hello-dep")
                        .endMetadata()
                        .withNewSpec()
                            .addNewContainer()
                                .withImage("gcr.io/google-samples/hello-app:1.0")
                                .withImagePullPolicy("Always")
                                .withName("hello-app")
                                .addNewPort()
                                    .withContainerPort(8080)
                                .endPort()
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                    .withNewStrategy()
                        .withType("RollingUpdate")
                        .withNewRollingUpdate()
                            .withMaxUnavailable(new IntOrString(0))
                            .withMaxSurge(new IntOrString(4))
                        .endRollingUpdate()
                    .endStrategy()
                .endSpec()
                .build();
    }
}
