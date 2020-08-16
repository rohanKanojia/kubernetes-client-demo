package io.fabric8;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;

public class CreateOrReplaceResourceList {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            List<HasMetadata> resources = client.load(CreateOrReplaceResourceList.class.getResourceAsStream("/test-list.yml")).get();
            KubernetesList kubernetesResourceList = new KubernetesListBuilder()
                    .build();
            kubernetesResourceList.setItems(resources);
            client.resourceList(kubernetesResourceList).inNamespace("default").deletingExisting().createOrReplace();
        }
    }
}