package io.fabric8.programming.kubernetes.java.dynamicclient;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import static io.fabric8.programming.kubernetes.java.dynamicclient.GenericKubernetesResourceDemo.createNewCronTab;

public class GenericKubernetesResourceFirstEntrypoint {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      GenericKubernetesResource genericKubernetesResource = createNewCronTab();
                                           // ApiVersion               // Kind
      client.genericKubernetesResources("stable.example.com/v1", "CronTab")
          .inNamespace("default")
          .resource(genericKubernetesResource)
          .create();
    }
  }
}
