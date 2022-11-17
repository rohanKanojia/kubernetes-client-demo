package io.fabric8.programming.kubernetes.java.dynamicclient;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;

import static io.fabric8.programming.kubernetes.java.dynamicclient.GenericKubernetesResourceDemo.createNewCronTab;

public class GenericKubernetesResourceSecondEntryPoint {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      GenericKubernetesResource genericKubernetesResource = createNewCronTab();

      ResourceDefinitionContext context = new ResourceDefinitionContext.Builder()
          .withGroup("stable.example.com")
          .withVersion("v1")
          .withKind("CronTab")
          .withPlural("crontabs")
          .withNamespaced(true)
          .build();
      client.genericKubernetesResources(context)
          .inNamespace("default")
          .resource(genericKubernetesResource)
          .create();
    }
  }
}
