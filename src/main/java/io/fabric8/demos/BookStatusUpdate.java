package io.fabric8.demos;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.crd.model.v1alpha1.BookStatus;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookStatusUpdate {
    private static MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient;

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Book Client
            bookClient = client.resources(Book.class);

            // Get Book
            Book book = bookClient.inNamespace("default").withName("effective-java").get();

            // Update Book Status
            book.setStatus(createBookStatus(true, "Rohan Kumar"));
            bookClient.inNamespace("default").resource(book).replaceStatus();
        }
    }

    private static BookStatus createBookStatus(boolean isIssued, String issuedTo) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setIssued(isIssued);
        bookStatus.setIssuedto(issuedTo);
        return bookStatus;
    }
}
