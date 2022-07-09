package io.fabric8;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class CreateOrReplaceDemo {
    private static final Logger logger = LoggerFactory.getLogger(CreateOrReplaceDemo.class);

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            String namespace = "default";

            Service service =  new ServiceBuilder()
                    .withNewMetadata().withName("my-service").endMetadata()
                    .withNewSpec()
                    .addToSelector("app", "Myapp")
                    .addNewPort().withProtocol("TCP").withPort(80).withTargetPort(new IntOrString(9376)).endPort()
                    .endSpec()
                    .build();


            ConfigMap configMap = new ConfigMapBuilder()
                    .withNewMetadata().withName("my-configmap").endMetadata()
                    .addToData(Collections.singletonMap("app", "Myapp"))
                    .build();

            logger.info("1. Creating list...");
            KubernetesList list = new KubernetesListBuilder()
                    .withItems(client.apps().deployments().load(CreateOrReplaceDemo.class.getResourceAsStream("/nginx-deployment.yml")).get()
                    , service, configMap)
                    .build();

            logger.info("2. Applying list onto cluster...");
            client.resourceList(list).inNamespace(namespace).createOrReplace();

            logger.info("3. Modifying service to use new port ...");
            service.getSpec().getPorts().get(0).setTargetPort(new IntOrString(9999));
            list.getItems().remove(0);
            list.getItems().add(service);

            logger.info("4. Applying updated list...");
            client.resourceList(list).inNamespace(namespace).createOrReplace();

            logger.info("5. Deleting existing resources...");
            client.resourceList(list).inNamespace(namespace).delete();
        }
    }
}
