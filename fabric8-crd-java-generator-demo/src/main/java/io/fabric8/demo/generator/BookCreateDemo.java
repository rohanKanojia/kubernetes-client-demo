package io.fabric8.demo.generator;

import io.fabric8.demo.generator.v1alpha1.Book;
import io.fabric8.demo.generator.v1alpha1.BookBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class BookCreateDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      Book book1 = createNewBook("kubernetes-patterns", "Kubernetes Patterns",
          "Bilgin Ibryam, Roland Huß", "9781492050285");

      client.resources(Book.class)
          .inNamespace("default")
          .resource(book1)
          .create();

      Book book2 = createNewBook("kubernetes-in-action", "Kubernetes in Action",
          "Marko Luksa", "9781617293726");
      client.resources(Book.class)
          .inNamespace("default")
          .resource(book2)
          .create();
    }
  }

  private static Book createNewBook(String name, String title, String author, String isbn) {
    return new BookBuilder()
        .withNewMetadata().withName(name).endMetadata()
        .withNewSpec()
        .withTitle(title)
        .withAuthor(author)
        .withIsbn(isbn)
        .endSpec()
        .build();
  }
}
