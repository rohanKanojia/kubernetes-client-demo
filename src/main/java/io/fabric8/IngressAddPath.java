package io.fabric8;

import io.fabric8.kubernetes.api.model.networking.v1.IngressBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class IngressAddPath {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
client.network().v1()
    .ingresses()
    .inNamespace("default")
    .withName("ingress-wildcard-host")
    .patch(PatchContext.of(PatchType.JSON), "[{\"op\":\"add\",\"path\":\"/spec/rules/0/http/paths/1\",\"value\":{\"backend\":{\"service\":{\"name\":\"service5\",\"port\":{\"number\":80}}},\"path\":\"/bar5\",\"pathType\":\"Prefix\"}}]");

client.network().v1()
    .ingresses()
    .inNamespace("default")
    .withName("ingress-wildcard-host")
    .edit(i -> new IngressBuilder(i)
        .editSpec()
          .editFirstRule()
            .editHttp()
              .addNewPath()
                .withPath("/bar4")
                .withPathType("Prefix")
                .withNewBackend()
                .withNewService()
                  .withName("service4")
                  .withNewPort().withNumber(80).endPort()
                .endService()
                .endBackend()
              .endPath()
            .endHttp()
          .endRule()
        .endSpec()
        .build());
    }
  }
}