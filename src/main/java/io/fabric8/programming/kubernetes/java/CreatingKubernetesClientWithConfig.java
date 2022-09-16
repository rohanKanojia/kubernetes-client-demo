package io.fabric8.programming.kubernetes.java;

import io.fabric8.kubernetes.api.model.APIGroup;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CreatingKubernetesClientWithConfig {
  public static void main(String[] args) {
try (KubernetesClient client = new KubernetesClientBuilder()
    .withConfig(new ConfigBuilder()
        .withMasterUrl("https://api.sandbox.x8i5.p1.openshiftapps.com:6443")
        .withOauthToken("sha256~5b1dLvxN1kAKF0-9zhP2FP_C3H0JSh_YfmGOAu-rSpA")
        .withNamespace("default")
        .withCaCertFile("/home/foo/.minikube/ca.crt")
        .withClientCertFile("/home/foo/.minikube/profiles/minikube/client.crt")
        .withClientKeyFile("/home/foo/.minikube/profiles/minikube/client.key")
        .withClientKeyAlgo("RSA")
        .build())
    .build()) {

  client.getApiGroups().getGroups().stream()
      .map(APIGroup::getName)
      .forEach(System.out::println);
}
  }
}
