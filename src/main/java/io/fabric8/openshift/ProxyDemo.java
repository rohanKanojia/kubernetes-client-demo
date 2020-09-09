package io.fabric8.openshift;

import io.fabric8.openshift.api.model.APIServerList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProxyDemo {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            APIServerList apiServerList = client.config().apiServers().list();
            apiServerList.getItems()
                    .forEach(a -> System.out.println(a.getMetadata().getName()));
        }
    }
}