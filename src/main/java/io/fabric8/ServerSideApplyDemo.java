package io.fabric8;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

import java.util.Collections;

public class ServerSideApplyDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      Service svc = client.services()
          .load(ServerSideApplyDemo.class.getResourceAsStream("/test-svc.yaml"))
          .get();

      client.services()
          .inNamespace("default")
          .resource(svc)
          .create();

      svc.getMetadata().setAnnotations(Collections.singletonMap("f1", "v1"));

      client.services()
          .inNamespace("default")
          .withName("my-service")
          .patch(PatchContext.of(PatchType.SERVER_SIDE_APPLY), svc);
    }
  }
}
