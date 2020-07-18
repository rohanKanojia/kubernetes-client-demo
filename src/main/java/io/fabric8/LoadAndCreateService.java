package io.fabric8;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class LoadAndCreateService {
    public static void main(String[] args) {
        
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Service svc = client.services()
                    .load(LoadAndCreateService.class.getResourceAsStream("/test-svc.yaml"))
                    .get();

            client.services().inNamespace("rokumar").createOrReplace(svc);
        }

        
    }
}
