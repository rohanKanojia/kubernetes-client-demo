package io.fabric8.programming.kubernetes.java;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class PatchDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.services()
          .inNamespace("default")
          .withName("my-service")
          .patch(PatchContext.of(PatchType.STRATEGIC_MERGE), "{\"metadata\":{\"annotations\":{\"foo\":\"bar\"}}}");
    }
  }
}
