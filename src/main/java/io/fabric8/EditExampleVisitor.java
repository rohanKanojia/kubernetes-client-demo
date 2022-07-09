package io.fabric8;

import io.fabric8.kubernetes.api.builder.TypedVisitor;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class EditExampleVisitor {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.pods().inNamespace("default").withName("exf6fv2")
                    .edit(new TypedVisitor<ObjectMetaBuilder>() {
                        @Override
                        public void visit(ObjectMetaBuilder o) {
                            o.addToAnnotations("two", "2");
                        }
                    });
        }
    }
}
