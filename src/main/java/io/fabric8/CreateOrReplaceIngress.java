package io.fabric8;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Map;

public class CreateOrReplaceIngress {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            Ingress ing = client.network().v1().ingresses().load(CreateOrReplaceIngress.class.getResourceAsStream("/test-ing.yml")).item();

            ObjectMeta meta = ing.getMetadata();
            Map<String, String> annotations = meta.getAnnotations();
            annotations.put("foo", "bar");
            meta.setAnnotations(annotations);
            ing.setMetadata(meta);
            client.network().v1().ingresses().inNamespace("default").resource(ing).createOrReplace();
        }
    }
}