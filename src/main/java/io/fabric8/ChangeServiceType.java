package io.fabric8;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ChangeServiceType {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Service svc = client.services().load(ChangeServiceType.class.getResourceAsStream("/test-svc.yaml")).get();

            // Create
            client.services().inNamespace("default").createOrReplace(svc);

            svc.getSpec().setType("ExternalName");
            svc.getSpec().setExternalName("my.database.example.com");
            svc.getSpec().setClusterIP("");
            svc = client.services().inNamespace("default").createOrReplace(svc);

        }
    }
}