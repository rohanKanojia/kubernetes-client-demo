package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class RunDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      String namespace = "default";
      String name = "run-config-example";
      String image = "alpine:3.19.0";

      client.run().inNamespace(namespace).withNewRunConfig()
          .withRestartPolicy("Never")
          .withName(name)
          .withImage(image)
          .withArgs("sh", "-c", "trap : TERM INT; sleep infinity & wait")
          .done();
      System.out.println("Done");
    }
  }
}
