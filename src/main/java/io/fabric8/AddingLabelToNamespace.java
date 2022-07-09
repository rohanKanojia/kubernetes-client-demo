package io.fabric8;

import io.fabric8.kubernetes.api.builder.TypedVisitor;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class AddingLabelToNamespace {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.namespaces().withName("default").edit(new TypedVisitor<ObjectMetaBuilder>() {
                @Override
                public void visit(ObjectMetaBuilder omb) {
                    omb.addToLabels("core.k8s/rbac-processed", String.valueOf(true));
                }
            });
        }
    }
}