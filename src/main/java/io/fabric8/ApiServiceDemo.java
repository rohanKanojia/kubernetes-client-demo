package io.fabric8;

import io.fabric8.kubernetes.api.model.APIService;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ApiServiceDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.apiServices().list().getItems()
        .stream()
        .map(APIService::getMetadata)
        .map(ObjectMeta::getName)
        .forEach(System.out::println);
    }
  }
}
