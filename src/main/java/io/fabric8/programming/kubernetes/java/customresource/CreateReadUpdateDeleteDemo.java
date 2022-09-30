package io.fabric8.programming.kubernetes.java.customresource;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.fabric8.programming.kubernetes.java.customresource.BookUtil.createNewBook;

public class CreateReadUpdateDeleteDemo {
  private static final Logger logger = LoggerFactory.getLogger(CreateReadUpdateDeleteDemo.class.getSimpleName());

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      // Create Book Client
      MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookOp = client.resources(Book.class);

      // Create Book Object
      Book book1 = createNewBook("head-first-java", "Head First Java", "Kathy Sierra", "9781491910771");

      // Create
      bookOp.inNamespace("default").resource(book1).create();

      // Read
      book1 = bookOp.inNamespace("default").withName("head-first-java").get();

      // List
      KubernetesResourceList<Book> books = bookOp.inNamespace("default").list();
      books.getItems().stream().map(CustomResource::getMetadata).map(ObjectMeta::getName).forEach(logger::info);

      // Update
      book1.getSpec().setAuthor("Kathy Sierra, Bert Bates, Trisha Gee");
      bookOp.inNamespace("default").resource(book1).replace();

      // Delete
      bookOp.inNamespace("default").resource(book1).delete();
    }
  }

}
