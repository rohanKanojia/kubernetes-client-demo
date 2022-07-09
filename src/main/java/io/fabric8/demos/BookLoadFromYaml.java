package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookLoadFromYaml {
    private static MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = null;

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Book Client
            bookClient = client.resources(Book.class);

            // Load Book from YAML manifest
            Book book = bookClient.load(BookLoadFromYaml.class.getResourceAsStream("/book-cr.yaml")).get();
        }
    }
}
