package io.fabric8;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.resource.v1alpha1.ResourceClaim;
import io.fabric8.kubernetes.api.model.resource.v1alpha1.ResourceClaimBuilder;
import io.fabric8.kubernetes.api.model.resource.v1alpha1.ResourceClaimList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.stream.Collectors;

public class ResourceClaimDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      ResourceClaim ps = new ResourceClaimBuilder()
          .withNewMetadata()
          .withName("test-resourceclaim")
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
          .build();

      client.dynamicResourceAllocation()
          .v1alpha1()
          .resourceClaims()
          .inNamespace("default")
          .resource(ps)
          .create();

      ResourceClaimList psList = client.dynamicResourceAllocation()
          .v1alpha1()
          .resourceClaims()
          .inNamespace("default")
          .list();
      System.out.printf("Found %s ResourceClaim items%n", psList.getItems().size());
      System.out.printf("%s%n", psList.getItems().stream()
          .map(ResourceClaim::getMetadata)
          .map(ObjectMeta::getName)
          .collect(Collectors.toSet()));
    }
  }
}
