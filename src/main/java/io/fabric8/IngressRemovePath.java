package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class IngressRemovePath {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.network().v1()
          .ingresses()
          .inNamespace("default")
          .withName("ingress-wildcard-host")
          .patch(PatchContext.of(PatchType.JSON), "[{\"op\": \"remove\", \"path\":\"/spec/rules/0/http/paths/0\"}]");
    }
  }
}
