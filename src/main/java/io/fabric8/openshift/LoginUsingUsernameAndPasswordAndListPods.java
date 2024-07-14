package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class LoginUsingUsernameAndPasswordAndListPods {
  public static void main(String[] args) {
    Config config = new ConfigBuilder(Config.empty())
      .withMasterUrl("https://api.crc.testing:6443")
      .withUsername("kubeadmin")
      .withPassword("IHiBd-9zJ6H-vICp4-Lpd69")
      .withTrustCerts()
      .build();

    try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().withConfig(config).build()) {
      OpenShiftClient openShiftClient = kubernetesClient.adapt(OpenShiftClient.class);

      openShiftClient.pods().list().getItems().stream().map(Pod::getMetadata).map(ObjectMeta::getName).forEach(System.out::println);
    }
  }
}
