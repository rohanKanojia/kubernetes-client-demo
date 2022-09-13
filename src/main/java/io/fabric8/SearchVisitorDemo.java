package io.fabric8;

import io.fabric8.kubernetes.api.model.APIResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.ApiVisitor;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.concurrent.CompletableFuture;

public class SearchVisitorDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {

      CompletableFuture<GenericKubernetesResourceList> done = new CompletableFuture<>();
      // Find all deployments with label app=nginx
      client.visitResources(new ApiVisitor() {
        @Override
        public ApiVisitResult visitApiGroup(String group) {
          return ApiVisitResult.CONTINUE;
        }


        @Override
        public ApiVisitResult visitResource(String group, String version, APIResource apiResource, MixedOperation<GenericKubernetesResource, GenericKubernetesResourceList, Resource<GenericKubernetesResource>> mixedOperation) {
          if (apiResource.getName().equals("deployments")) {
            done.complete(mixedOperation.withLabel("app", "nginx").list());
            return ApiVisitResult.TERMINATE;
          }
          return ApiVisitResult.CONTINUE;
        }
      });

      GenericKubernetesResourceList foundResources = done.join();
      System.out.println("Search Complete. Found " + foundResources.getItems().size() + " resources with label app=nginx.");
      foundResources.getItems()
          .stream()
          .map(GenericKubernetesResource::getMetadata)
          .map(ObjectMeta::getName)
          .forEach(System.out::println);
    }
  }
}
