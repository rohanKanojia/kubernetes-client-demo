package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.openshift.client.OpenShiftClient;

public class PodDumpAsYaml {
  public static void main(String[] args) {
    try (OpenShiftClient oClient = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
      String npName = "rokumar-dev";
      String podName = "pod-with-annotation";
      Pod pod = oClient.pods().inNamespace(npName).withName(podName).get();
      System.out.println(Serialization.asYaml(pod));
    }
  }
}
