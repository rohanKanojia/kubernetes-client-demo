package io.fabric8;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;

public class PodExecGetCommandStatusCode {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Pod pod = client.pods()
          .load(PodExecGetCommandStatusCode.class.getResourceAsStream("/test-pod.yml"))
          .get();

      client.pods().inNamespace("default").resource(pod).create();

      ExecWatch execWatch = client.pods().inNamespace("default").withName("example-pod")
          .writingOutput(System.out)
          .writingError(System.out)
          .exec("/bin/ping", "goo");
      Thread.sleep(5 * 1000L);

      System.out.println(baos);
      int exitCode = execWatch.exitCode().get();
      System.out.println(exitCode);
      execWatch.close();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }
}
