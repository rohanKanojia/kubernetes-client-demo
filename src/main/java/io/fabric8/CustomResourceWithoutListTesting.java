package io.fabric8;

import io.fabric8.crd.CronTab;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class CustomResourceWithoutListTesting {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            MixedOperation<CronTab, KubernetesResourceList<CronTab>, Resource<CronTab>> cronTabClient = client.customResources(CronTab.class);

            KubernetesResourceList<CronTab> list = cronTabClient.inNamespace("default").list();
            System.out.println(list.getItems().size());
        }
    }
}
