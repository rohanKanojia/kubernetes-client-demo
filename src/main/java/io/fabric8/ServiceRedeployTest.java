package io.fabric8;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.net.UnknownHostException;
import java.util.Collections;

public class ServiceRedeployTest {
    public static void main(String[] args) {
try (KubernetesClient client = new DefaultKubernetesClient()) {
    Service svc = client.services().load(ServiceRedeployTest.class.getResourceAsStream("/test-random-service.yml"))
            .get();

//            client.services().inNamespace("default").create(svc);
    svc.getMetadata().setAnnotations(Collections.singletonMap("foo", "bnar"));
    client.services().inNamespace("default").withName(svc.getMetadata().getName())
            .patch(new ServiceBuilder(svc).build());

//            client.services().inNamespace("default").withName(svc.getMetadata().getName()).delete();
} catch (KubernetesClientException e) {
    if (e.getCause() instanceof UnknownHostException) {
        System.out.println("No Kubernetes host found");
    }
}
    }
}
