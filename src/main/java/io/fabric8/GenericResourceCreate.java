package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class GenericResourceCreate {
    public static void main(String[] args) {
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            Deployment deployment = client.apps().deployments()
                    .load(GenericResourceCreate.class.getResourceAsStream("/nginx-deployment.yml"))
                    .get();

            client.resource(deployment).deletingExisting().createOrReplace();
        }
    }
}
