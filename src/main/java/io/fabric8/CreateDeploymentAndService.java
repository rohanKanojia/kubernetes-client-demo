package io.fabric8;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Collections;

public class CreateDeploymentAndService {
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
            client.apps().deployments().inNamespace("default").create(deployment);

            Service service = new ServiceBuilder()
                    .withNewMetadata().withName("nginx-svc").addToLabels("app", "nginx").endMetadata()
                    .withNewSpec()
                    .addNewPort()
                    .withName("http")
                    .withPort(80)
                    .withTargetPort(new IntOrString(80))
                    .endPort()
                    .withSelector(Collections.singletonMap("app", "nginx"))
                    .withType("NodePort")
                    .endSpec()
                    .build();
            client.services().inNamespace("default").create(service);
        }
    }
}
