package io.fabric8;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ServiceCreateOrReplace {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            Service svc = new ServiceBuilder()
                    .withNewMetadata().withName("my-service").endMetadata()
                    .withNewSpec()
                    .withType("NodePort")
                    .addToSelector("app", "MyApp")
                    .addNewPort()
                    .withPort(80)
                    .withTargetPort(new IntOrString(80))
                    .endPort()
                    .endSpec()
                    .build();

            Service svc1 = client.services().inNamespace("rokumar").resource(svc).createOrReplace();
            System.out.println(svc1.getSpec().getPorts().get(0).getNodePort());
            Service svc2 = client.services().inNamespace("rokumar").resource(svc).createOrReplace();
            System.out.println(svc2.getSpec().getPorts().get(0).getNodePort());
        }
    }
}