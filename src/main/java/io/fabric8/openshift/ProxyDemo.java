package io.fabric8.openshift;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.api.model.config.v1.APIServerList;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProxyDemo {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            APIServerList apiServerList = client.config().apiServers().list();
            apiServerList.getItems()
                    .forEach(a -> System.out.println(a.getMetadata().getName()));
        }
    }
}