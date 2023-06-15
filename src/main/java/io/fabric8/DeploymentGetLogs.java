package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeploymentGetLogs {
  private static final Logger logger = LoggerFactory.getLogger(DeploymentGetLogs.class.getSimpleName());
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      // This microservice is already deployed using Kubernetes maven plugin
      String log = client.apps().deployments().inNamespace("default")
          .withName("random-generator")
          .getLog();
      logger.info(log);
    }
  }
}
