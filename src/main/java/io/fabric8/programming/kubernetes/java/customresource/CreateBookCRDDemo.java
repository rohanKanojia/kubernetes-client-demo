package io.fabric8.programming.kubernetes.java.customresource;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CreateBookCRDDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.apiextensions().v1()
          .customResourceDefinitions()
          .load(CreateBookCRDDemo.class.getResourceAsStream("/book-crd.yaml"))
          .create();
    }
  }
}
