package io.fabric8;

import io.fabric8.kubernetes.api.model.APIGroupList;
import io.fabric8.kubernetes.api.model.APIResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class GetApiResourcesDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      APIGroupList apiGroupList = client.getApiGroups();

      apiGroupList.getGroups()
          .forEach(group -> group.getVersions().forEach(gv -> {
            APIResourceList apiResourceList = client.getApiResources(gv.getGroupVersion());
            apiResourceList.getResources()
                .stream()
                .filter(r -> !r.getName().contains("/"))
                .forEach(r -> System.out.printf("%s  %s   %s   %s   %s%n", r.getName(), String.join(",", r.getShortNames()),
                    gv.getGroupVersion(), r.getNamespaced(), r.getKind()));
          }));
    }
  }
}
