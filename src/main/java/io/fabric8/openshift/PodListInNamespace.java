package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.fabric8.kubernetes.client.utils.KubernetesResourceUtil.getAge;

public class PodListInNamespace {
  private static final Logger logger = LoggerFactory.getLogger(PodListInNamespace.class.getName());

  public static void main(String[] args) {
    try (OpenShiftClient k8sClient = new DefaultOpenShiftClient()) {
      k8sClient.pods().inNamespace("rokumar-dev").list().getItems()
          .forEach(p -> System.out.printf("%s %s %s %s%n", p.getMetadata().getNamespace(), p.getMetadata().getName(), p.getStatus().getPhase(), getAge(p).toString().substring(2)
            .toLowerCase()));

    } catch (KubernetesClientException exception) {
      logger.info("error: {}", exception.getMessage());
    }
  }
}
