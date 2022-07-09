package io.fabric8;

import java.util.Map;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ResourceAPINPE {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.resources(HasMetadata.class)
          .inNamespace("default")
          .withLabels(Map.of("foo", "bar"))
          .list();
    }
  }
}
