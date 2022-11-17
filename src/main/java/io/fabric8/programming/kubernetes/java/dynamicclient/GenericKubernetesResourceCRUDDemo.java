package io.fabric8.programming.kubernetes.java.dynamicclient;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.fabric8.programming.kubernetes.java.dynamicclient.GenericKubernetesResourceDemo.createNewCronTab;

public class GenericKubernetesResourceCRUDDemo {
  private static final Logger logger = LoggerFactory.getLogger(GenericKubernetesResourceCRUDDemo.class);

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      // Create CronTab context
      ResourceDefinitionContext context = new ResourceDefinitionContext.Builder()
          .withGroup("stable.example.com")
          .withVersion("v1")
          .withKind("CronTab")
          .withPlural("crontabs")
          .withNamespaced(true)
          .build();

      // Create CronTab Object
      GenericKubernetesResource genericKubernetesResource = createNewCronTab();

      // Create
      client.genericKubernetesResources(context)
          .inNamespace("default")
          .resource(genericKubernetesResource)
          .create();

      // Read
      genericKubernetesResource = client.genericKubernetesResources(context)
          .inNamespace("default")
          .withName("my-new-cron-object")
          .get();

      // List
      GenericKubernetesResourceList cronTabs = client.genericKubernetesResources(context).inNamespace("default").list();
      cronTabs.getItems().stream().map(GenericKubernetesResource::getMetadata).map(ObjectMeta::getName).forEach(logger::info);

      // Update
      Map<String, Object> additionalProperties = genericKubernetesResource.getAdditionalProperties();
      Map<String, Object> spec = (Map<String, Object>) additionalProperties.get("spec");
      spec.put("image", "my-updated-cron-image");
      client.genericKubernetesResources(context).inNamespace("default").resource(genericKubernetesResource).replace();

      // Delete
      client.genericKubernetesResources(context).inNamespace("default").resource(genericKubernetesResource).delete();
    }
  }
}
