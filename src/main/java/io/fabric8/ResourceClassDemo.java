package io.fabric8;

import io.fabric8.kubernetes.api.model.NodeSelectorBuilder;
import io.fabric8.kubernetes.api.model.NodeSelectorRequirementBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.resource.v1alpha2.ResourceClass;
import io.fabric8.kubernetes.api.model.resource.v1alpha2.ResourceClassBuilder;
import io.fabric8.kubernetes.api.model.resource.v1alpha2.ResourceClassList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.stream.Collectors;

public class ResourceClassDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      ResourceClass ps = new ResourceClassBuilder()
          .withNewMetadata()
          .withName("example-rc")
          .endMetadata()
          .withNewParametersRef()
          .withKind("kindValue")
          .withName("nameValue")
          .withApiGroup("apiGroupValue")
          .endParametersRef()
          .withDriverName("driver-name")
          .withSuitableNodes(new NodeSelectorBuilder()
              .addNewNodeSelectorTerm()
              .addToMatchExpressions(new NodeSelectorRequirementBuilder()
                  .withKey("environment")
                  .withOperator("In")
                  .addToValues("dev")
                  .build())
              .addToMatchFields(new NodeSelectorRequirementBuilder()
                  .withKey("metadata.name")
                  .withOperator("In")
                  .addToValues("backend")
                  .build())
              .endNodeSelectorTerm()
              .build())
          .build();

      client.dynamicResourceAllocation()
          .v1alpha2()
          .resourceClasses()
          .resource(ps)
          .create();

      ResourceClassList psList = client.dynamicResourceAllocation()
          .v1alpha2()
          .resourceClasses()
          .list();
      System.out.printf("Found %s ResourceClass items%n", psList.getItems().size());
      System.out.printf("%s", psList.getItems().stream()
          .map(ResourceClass::getMetadata)
          .map(ObjectMeta::getName)
          .collect(Collectors.toSet()));
    }
  }
}
