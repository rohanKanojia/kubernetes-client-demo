package io.fabric8.openshift;

import io.fabric8.openshift.api.model.Ingress;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class OpenShiftConfigIngress {
  public static void main(String[] args) {
    try (OpenShiftClient client = new DefaultOpenShiftClient()) {
      Ingress ingress = client.config().ingresses().withName("cluster").get();
      System.out.println("Ingress " + ingress.getMetadata().getName() + " found.");
    }
  }
}
