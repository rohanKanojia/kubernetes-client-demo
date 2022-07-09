package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class GenericKubernetesResourceNoContextExample {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.genericKubernetesResources("stable.example.com/v1", "CronTab")
          .inNamespace("default")
          .load(GenericKubernetesResourceNoContextExample.class.getResource("/crontab.yml"))
          .create();
    }
  }
}
