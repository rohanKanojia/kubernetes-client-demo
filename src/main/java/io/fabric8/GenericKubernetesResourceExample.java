package io.fabric8;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

public class GenericKubernetesResourceExample {
  public static void main(String[] args) {
    try (KubernetesClient client = new DefaultKubernetesClient()) {
      CustomResourceDefinitionContext context = new CustomResourceDefinitionContext.Builder()
          .withGroup("sparkoperator.k8s.io")
          .withScope("Namespaced")
          .withVersion("v1beta2")
          .withPlural("sparkapp")
          .build();

      GenericKubernetesResource cr = client.genericKubernetesResources(context)
          .load(GenericKubernetesResourceExample.class.getResourceAsStream("/sparkapplication-cr.yml"))
          .get();

      client.genericKubernetesResources(context)
          .inNamespace("default")
          .create(cr);
    }
  }
}
