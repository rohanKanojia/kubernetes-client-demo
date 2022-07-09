package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class DeploymentDeleteLivenessProbe {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.apps()
          .deployments()
          .inNamespace("default")
          .withName("random-generator")
          .patch(PatchContext.of(PatchType.JSON), "[{\"op\": \"remove\", \"path\":\"/spec/template/spec/containers/0/livenessProbe\"}]");
    }
  }
}
