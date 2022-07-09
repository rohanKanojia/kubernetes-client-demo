package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookListAllInNamespace {

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Book Client
            MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = client.resources(Book.class);

            // List all books in default namespace
            KubernetesResourceList<Book> books = bookClient.inNamespace("default").list();
            books.getItems().stream().map(CustomResource::getMetadata).map(ObjectMeta::getName).forEach(System.out::println);
        }
    }
}
