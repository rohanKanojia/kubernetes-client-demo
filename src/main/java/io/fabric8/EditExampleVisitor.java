package io.fabric8;

import io.fabric8.kubernetes.api.builder.TypedVisitor;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class EditExampleVisitor {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
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
