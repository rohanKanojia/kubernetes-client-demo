package io.fabric8;

import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class EditExampleBuilder {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.pods().inNamespace("default").withName("exf6fv2").edit(p -> new PodBuilder(p)
                    .editOrNewMetadata().addToAnnotations("one", "1").endMetadata().build());
        }
    }
}
