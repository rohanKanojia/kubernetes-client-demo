package io.fabric8.programming.kubernetes.java;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class CreateReadUpdateDeleteDemo {
  private static final Logger logger = LoggerFactory.getLogger(CreateReadUpdateDeleteDemo.class.getSimpleName());

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      // Load the Service
      Service svc = client.services()
          .load(CreateReadUpdateDeleteDemo.class.getResourceAsStream("/test-svc.yaml"))
          .get();

      // Create a Service
      client.services()
          .inNamespace("default")
          .resource(svc)
          .create();

      // List Service
      ServiceList svcList = client.services().inNamespace("default").list();
      logger.info("Found {} Services", svcList.getItems().size());


      // Update Service
      svc.getMetadata().setLabels(Collections.singletonMap("foo", "bar"));
      client.services()
          .inNamespace("default")
          .resource(svc)
          .update();

      // Delete Service
      client.services()
          .inNamespace("default")
          .resource(svc)
          .delete();
    }
  }
}
