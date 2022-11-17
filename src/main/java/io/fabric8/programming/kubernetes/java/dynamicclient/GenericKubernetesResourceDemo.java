package io.fabric8.programming.kubernetes.java.dynamicclient;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;

import java.util.HashMap;
import java.util.Map;

public class GenericKubernetesResourceDemo {
  public static void main(String[] args) {
    GenericKubernetesResource genericKubernetesResource = createNewCronTab();
    System.out.println(Serialization.asYaml(genericKubernetesResource));
  }

  public static GenericKubernetesResource createNewCronTab() {
    Map<String, Object> spec = new HashMap<>();
    spec.put("cronSpec", "* * * * */5");
    spec.put("image", "my-awesome-cron-image");

    return new GenericKubernetesResourceBuilder()
        .withApiVersion("stable.example.com/v1")
        .withKind("CronTab")
        .withNewMetadata()
        .withName("my-new-cron-object")
        .endMetadata()
        .addToAdditionalProperties("spec", spec)
        .build();
  }
}
