package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class DeleteReadinessProbe {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      Deployment deploymentWithReadinessProbe = client.apps().deployments()
        .load(DeleteReadinessProbe.class.getResourceAsStream("/deployment-with-readinessprobe.yml"))
        .item();

      client.apps().deployments().inNamespace("default").resource(deploymentWithReadinessProbe).serverSideApply();

      client.apps().deployments()
        .inNamespace("default")
        .withName("deployment-readinessprobe")
        .patch(PatchContext.of(PatchType.JSON), "[{\"op\": \"remove\", \"path\":\"/spec/template/spec/containers/0/readinessProbe\"}]");
    }
  }
}
