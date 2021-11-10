package io.fabric8;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PodListInNamespace {
  private static final Logger logger = LoggerFactory.getLogger(PodListInNamespace.class.getName());

  public static void main(String[] args) {
    try (KubernetesClient k8sClient = new DefaultKubernetesClient()) {
      PodList podList = k8sClient.pods().inNamespace("rokumar-dev").list();
      logger.info("There are {} pods in myns namespace.", podList.getItems().size());
    } catch (KubernetesClientException exception) {
      logger.info("error: {}", exception.getMessage());
    }
  }
}
