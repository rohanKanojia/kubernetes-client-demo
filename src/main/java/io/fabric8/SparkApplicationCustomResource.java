package io.fabric8;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SparkApplicationCustomResource {
    private static final Logger logger = Logger.getLogger(SparkApplicationCustomResource.class.getName());

    public static void main(String[] args) {
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            CustomResourceDefinition sparkCrd = client.apiextensions().v1beta1().customResourceDefinitions()
                    .load(SparkApplicationCustomResource.class.getResourceAsStream("/sparkapplication-crd.yml"))
                    .get();
            client.apiextensions().v1beta1().customResourceDefinitions().create(sparkCrd);
            log("Custom Resource Definition " + sparkCrd.getMetadata().getName() + " created.");

            CustomResourceDefinitionContext customResourceDefinitionContext = new CustomResourceDefinitionContext.Builder()
                    .withGroup("sparkoperator.k8s.io")
                    .withName("sparkapplications.sparkoperator.k8s.io")
                    .withPlural("sparkapplications")
                    .withScope("Namespaced")
                    .withVersion("v1beta1")
                    .build();

            // Create Spark Application Custom Resource
            GenericKubernetesResource sparkCustomResource = client.genericKubernetesResources(customResourceDefinitionContext)
                .load(SparkApplicationCustomResource.class.getResourceAsStream("/sparkapplication-cr.yml"))
                .create();

            log("Custom resource " + sparkCustomResource.getMetadata().getName() + " created");

            // Add volume
            Map<String, Object> configMapVolume = new HashMap<>();
            configMapVolume.put("name", "app-config");
            configMapVolume.put("configMap", Collections.singletonMap("name", "app-conf"));
            Map<String, Object> sparkCustomResourceSpec = sparkCustomResource.get("spec");
            sparkCustomResourceSpec.put("volumes", Collections.singletonList(configMapVolume));

            sparkCustomResource.setAdditionalProperties(Collections.singletonMap("spec", sparkCustomResourceSpec));

            client.genericKubernetesResources(customResourceDefinitionContext).inNamespace("default").withName(sparkCustomResource.getMetadata().getName()).replace(sparkCustomResource);
            log("Edition of custom resource successful.");

        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
