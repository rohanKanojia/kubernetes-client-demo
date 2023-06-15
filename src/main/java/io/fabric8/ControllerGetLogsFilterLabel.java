package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerGetLogsFilterLabel {
  private static final Logger logger = LoggerFactory.getLogger(ControllerGetLogsFilterLabel.class.getSimpleName());
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      // This microservice is already deployed using Kubernetes maven plugin
      PodList podList = client.pods()
          .inNamespace("default")
          .withLabel("app", "random-generator")
          .list();
      if (podList.getItems().isEmpty()) {
        logger.info("No pod with provided label found in cluster");
      } else {
        Pod pod = podList.getItems().get(0);
        String log = client.pods().inNamespace("default")
            .resource(pod)
            .getLog();
        logger.info(log);
      }
    }
  }
}
