package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.Collections;

public class BookUpdate {
    private static MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = null;

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
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
