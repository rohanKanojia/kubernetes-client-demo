package io.fabric8.openshift;

import io.fabric8.openshift.api.model.config.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class OpenShiftConfigIngress {
  public static void main(String[] args) {
    try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
      Ingress ingress = client.config().ingresses().withName("cluster").get();
      System.out.println("Ingress " + ingress.getMetadata().getName() + " found.");
    }
  }
}
