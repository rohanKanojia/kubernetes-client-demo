package io.fabric8;

import io.fabric8.kubernetes.api.model.autoscaling.v1.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.autoscaling.v1.HorizontalPodAutoscalerBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class HorizontalPodAutoscalerDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      HorizontalPodAutoscaler hpa = createHPA();
      client.autoscaling().v1().horizontalPodAutoscalers()
          .resource(hpa)
          .create();
      System.out.println(hpa.getMetadata().getName() + " created!");

      client.autoscaling().v1().horizontalPodAutoscalers()
          .resource(hpa)
          .delete();
      System.out.println(hpa.getMetadata().getName() + " deleted!");
    }

  }

  private static HorizontalPodAutoscaler createHPA() {
    return new HorizontalPodAutoscalerBuilder()
        .withNewMetadata()
        .withName("foo-hpa")
        .addToLabels("name", "foo")
        .endMetadata()
        .withNewSpec()
        .withNewScaleTargetRef()
        .withApiVersion("apps/v1")
        .withKind("Deployment")
        .withName("foo")
        .endScaleTargetRef()
        .withMinReplicas(1)
        .withMaxReplicas(10)
        .endSpec()
        .build();
  }
}
