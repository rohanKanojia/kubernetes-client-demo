package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookClientCreateDemo {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            // Create Book Client
            MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = client.resources(Book.class);

            // List Books in a specified namespace
            String namespace = "default";
            KubernetesResourceList<Book> books = bookClient.inNamespace(namespace).list();
            System.out.println(books.getItems().size() + " books found in " + namespace + " namespace");
        }
    }
}
