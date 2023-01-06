package io.fabric8.demo.generator;

import io.fabric8.demo.generator.v1alpha1.Book;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class BookListDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.resources(Book.class)
          .inNamespace("default")
          .list()
          .getItems()
          .stream()
          .map(CustomResource::getMetadata)
          .map(ObjectMeta::getName)
          .forEach(System.out::println);
    }
  }
}
