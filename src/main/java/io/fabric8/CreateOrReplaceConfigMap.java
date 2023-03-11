package io.fabric8;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import static java.util.Collections.singletonMap;

public class CreateOrReplaceConfigMap {
    public static final String RESOURCE_NAME = "my-resource";
    public static final String NAMESPACE = "rokumar";

    public static ConfigMap getOriginalCM()  {
        return new ConfigMapBuilder()
                .withNewMetadata()
                .withName(RESOURCE_NAME)
                .withLabels(singletonMap("state", "new"))
                .endMetadata()
                .build();
    }

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            ConfigMap newResource = getOriginalCM();
            client.configMaps().inNamespace(NAMESPACE).resource(newResource).create();
            client.configMaps().inNamespace(NAMESPACE).resource(newResource).serverSideApply();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}