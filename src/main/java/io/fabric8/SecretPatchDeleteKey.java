package io.fabric8;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class SecretPatchDeleteKey {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      Secret secret = new SecretBuilder()
          .withNewMetadata().withName("test-path-null").endMetadata()
          .addToData("username", "cm9rdW1hci1kZXYK")
          .addToData("passwd", "c2VjcmV0Cg==")
          .build();

      client.secrets().inNamespace("default").resource(secret).create();

      client.secrets()
          .inNamespace("default")
          .withName("test-path-null")
          .patch(PatchContext.of(PatchType.JSON), "[{\"op\": \"remove\", \"path\":\"/data/username\"}]");
    }
  }
}
