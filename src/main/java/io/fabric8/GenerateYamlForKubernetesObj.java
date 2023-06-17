package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;

public class GenerateYamlForKubernetesObj {
  public static void main(String[] args) {
    try (KubernetesClient k8s = new KubernetesClientBuilder().build()) {
      Pod pod = k8s.pods()
          .inNamespace("default")
          .withName("excj496")
          .get();
      // Get YAML string
      System.out.println(Serialization.asYaml(pod));
      // Get JSON string
      System.out.println(Serialization.asJson(pod));
    }
  }
}
