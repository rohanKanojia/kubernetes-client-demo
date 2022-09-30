package io.fabric8.demos;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.crd.model.v1alpha1.BookSpec;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookPatch {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
//      client.apiextensions().v1()
//          .customResourceDefinitions()
//          .load(BookPatch.class.getResourceAsStream("/book-crd.yaml"))
//          .create();

      MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient =
          client.resources(Book.class);

//      Book book = bookClient.inNamespace("default").load(BookPatch.class.getResourceAsStream("/book-cr.yaml")).create();

      Book book = bookClient.inNamespace("default").withName("effective-java").get();
      BookSpec bookSpec = book.getSpec();
      bookSpec.setIsbn("rere");
      book.setSpec(bookSpec);

      bookClient.inNamespace("default").resource(book).patch();


    }
  }
}
