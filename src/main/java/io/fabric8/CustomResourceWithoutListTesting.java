package io.fabric8;

import io.fabric8.crd.mode.v1.CronTab;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class CustomResourceWithoutListTesting {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            MixedOperation<CronTab, KubernetesResourceList<CronTab>, Resource<CronTab>> cronTabClient = client.resources(CronTab.class);

            KubernetesResourceList<CronTab> list = cronTabClient.inNamespace("default").list();
            System.out.println(list.getItems().size());
        }
    }
}
