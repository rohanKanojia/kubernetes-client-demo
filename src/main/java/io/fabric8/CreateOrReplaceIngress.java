package io.fabric8;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.networking.v1beta1.Ingress;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Map;

public class CreateOrReplaceIngress {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Ingress ing = client.network().ingresses().load(CreateOrReplaceIngress.class.getResourceAsStream("/test-ing.yml")).get();

            ObjectMeta meta = ing.getMetadata();
            Map<String, String> annotations = meta.getAnnotations();
            annotations.put("foo", "bar");
            meta.setAnnotations(annotations);
            ing.setMetadata(meta);
            client.network().ingresses().inNamespace("default").createOrReplace(ing);
        }
    }
}