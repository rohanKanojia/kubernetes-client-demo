package io.fabric8.programming.kubernetes.java;

import io.fabric8.kubernetes.api.model.APIGroup;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CreatingKubernetesClient {
  public static void main(String[] args) {
try (KubernetesClient client = new KubernetesClientBuilder().build()) {

  client.getApiGroups().getGroups().stream()
      .map(APIGroup::getName)
      .forEach(System.out::println);
}
  }
}
