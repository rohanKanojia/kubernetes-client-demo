package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.PodResource;

import java.io.ByteArrayOutputStream;

public class ExecWithPipesDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      PodResource podResource = client.pods()
          .inNamespace("default")
          .withName("exrf9fr");
      ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
      try (ExecWatch execWatch = podResource.writingOutput(baos).terminateOnError()
          .exec("sh", "-c", "ls -lrt /proc | wc -l")) {
        execWatch.exitCode().join();
      }

      System.out.println("Result : " + baos);
    }
  }
}
