package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.crd.BookSpec;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookCreate {

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Book Client
            MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = client.resources(Book.class);

            // Create New Book Object
            Book book = createNewBook("kubernetes-patterns", "Kubernetes Patterns",
                    "Ibryam, Bilgin; Huß, Roland", "9781492050285");

            // Apply Book object in Kubernetes
            bookClient.inNamespace("default").resource(book).create();
        }
    }

    private static Book createNewBook(String name, String title, String author, String isbn) {
        BookSpec bookSpec = new BookSpec();
        bookSpec.setAuthor(author);
        bookSpec.setIsbn(isbn);
        bookSpec.setTitle(title);

        Book book = new Book();
        book.setMetadata(new ObjectMetaBuilder().withName(name).build());
        book.setSpec(bookSpec);

        return book;
    }
}
