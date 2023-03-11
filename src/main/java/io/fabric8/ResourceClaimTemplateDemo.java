package io.fabric8;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.resource.v1alpha1.ResourceClaimTemplate;
import io.fabric8.kubernetes.api.model.resource.v1alpha1.ResourceClaimTemplateBuilder;
import io.fabric8.kubernetes.api.model.resource.v1alpha1.ResourceClaimTemplateList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.stream.Collectors;

public class ResourceClaimTemplateDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      ResourceClaimTemplate ps = new ResourceClaimTemplateBuilder()
          .withNewMetadata()
          .withName("test-resourceclaimtemplate")
          .endMetadata()
          .withNewSpec()
          .withNewMetadata()
          .addToAnnotations("annotationKey", "annotationValue")
          .endMetadata()
          .withNewSpec()
          .withNewParametersRef()
          .withKind("kindValue")
          .withName("nameValue")
          .withApiGroup("apiGroupValue")
          .endParametersRef()
          .withAllocationMode("Immediate")
          .withResourceClassName("resource-class-name")
          .endSpec()
          .endSpec()
          .build();

      client.dynamicResourceAllocation()
          .v1alpha1()
          .resourceClaimTemplates()
          .inNamespace("default")
          .resource(ps)
          .create();

      ResourceClaimTemplateList psList = client.dynamicResourceAllocation()
          .v1alpha1()
          .resourceClaimTemplates()
          .inNamespace("default")
          .list();
      System.out.printf("Found %s ResourceClaimTemplate items%n", psList.getItems().size());
      System.out.printf("%s", psList.getItems().stream()
          .map(ResourceClaimTemplate::getMetadata)
          .map(ObjectMeta::getName)
          .collect(Collectors.toSet()));
    }
  }
}
