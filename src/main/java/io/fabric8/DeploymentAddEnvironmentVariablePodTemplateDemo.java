package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class DeploymentAddEnvironmentVariablePodTemplateDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      client.apps()
          .deployments()
          .inNamespace("default")
          .withName("jkube-spring-boot3-native-image-demo")
          .edit(d -> new DeploymentBuilder(d)
              .editSpec()
              .editTemplate()
              .editSpec()
              .editContainer(0)
              .addNewEnv()
              .withName("TEST_ENV_KEY")
              .withValue("TEST_ENV_VAL_UPDATED")
              .endEnv()
              .endContainer()
              .endSpec()
              .endTemplate()
              .endSpec()
              .build());
      client.apps()
          .deployments()
          .inNamespace("default")
          .withName("jkube-spring-boot3-native-image-demo")
          .patch(PatchContext.of(PatchType.STRATEGIC_MERGE),
          "{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"spring-boot\",\"env\":[{\"name\":\"PATCHED_ENV\", \"value\":\"PATCH_ENV_VAL_UPDATED\"}]}]}}}}");
    }
  }
}
