package io.fabric8.demos;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.Collections;

public class BookUpdate {
    private static MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = null;

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Book Client
            bookClient = client.resources(Book.class);

            // Update Book with name effective-java
            bookClient.inNamespace("default").withName("effective-java").edit(b -> {
                b.getMetadata().setAnnotations(Collections.singletonMap("updated", "true"));
                return b;
            });
        }
    }
}
