package io.fabric8;

import io.fabric8.kubernetes.api.model.authentication.v1alpha1.SelfSubjectReview;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class SelfSubjectReviewDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      SelfSubjectReview ssr = new SelfSubjectReview();
      SelfSubjectReview ssrFromServer = client.authentication().v1alpha1().selfSubjectReview().create(ssr);

      System.out.printf("Groups : [%s]%n", String.join(", ", ssrFromServer.getStatus().getUserInfo().getGroups()));
      System.out.printf("User : %s%n", ssrFromServer.getStatus().getUserInfo().getUsername());
    }
  }
}
