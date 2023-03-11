package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class GenericResourceCreate {
    public static void main(String[] args) {
        try (final KubernetesClient client = new KubernetesClientBuilder().build()) {
            Deployment deployment = client.apps().deployments()
                    .load(GenericResourceCreate.class.getResourceAsStream("/nginx-deployment.yml"))
                    .get();

            client.resource(deployment).create();
        }
    }
}
