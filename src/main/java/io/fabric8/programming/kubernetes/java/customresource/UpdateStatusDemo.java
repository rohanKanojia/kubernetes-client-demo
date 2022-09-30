package io.fabric8.programming.kubernetes.java.customresource;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.crd.model.v1alpha1.BookStatus;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import static io.fabric8.programming.kubernetes.java.customresource.BookUtil.createNewBook;

public class UpdateStatusDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookOp = client.resources(Book.class);

      // Create Book CR
      Book book1 = createNewBook("effective-java", "Effective Java", "Joshua Bloch ", "9788131726594");
      book1 = bookOp.inNamespace("default").resource(book1).create();

      // Create Status Object
      BookStatus book1Status = new BookStatus();
      book1Status.setIssued(true);
      book1Status.setIssuedto("Bob");

      // Update Status
      book1.setStatus(book1Status);
      bookOp.inNamespace("default").resource(book1).replaceStatus();
    }
  }
}
