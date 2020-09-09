package io.fabric8;

import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SparkApplicationCustomResource {
    private static final Logger logger = Logger.getLogger(SparkApplicationCustomResource.class.getName());

    public static void main(String args[]) throws IOException {

        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            CustomResourceDefinition sparkCrd = client.customResourceDefinitions()
                    .load(SparkApplicationCustomResource.class.getResourceAsStream("/sparkapplication-crd.yml"))
                    .get();
            client.customResourceDefinitions().create(sparkCrd);
            log("Custom Resource Definition " + sparkCrd.getMetadata().getName() + " created.");

            CustomResourceDefinitionContext customResourceDefinitionContext = new CustomResourceDefinitionContext.Builder()
                    .withGroup("sparkoperator.k8s.io")
                    .withName("sparkapplications.sparkoperator.k8s.io")
                    .withPlural("sparkapplications")
                    .withScope("Namespaced")
                    .withVersion("v1beta1")
                    .build();

            // Create Spark Application Custom Resource
            Map<String, Object> sparkCustomResource = client.customResource(customResourceDefinitionContext)
                    .create("default", SparkApplicationCustomResource.class.getResourceAsStream("/sparkapplication-cr.yml"));
            System.out.println(sparkCustomResource.toString());

            String customResourceName = ((Map<String, Object> )sparkCustomResource.get("metadata")).get("name").toString();
            log("Custom resource " + customResourceName + " created");

            // Add volume
            Map<String, Object> configMapVolume = new HashMap<>();
            configMapVolume.put("name", "app-config");
            configMapVolume.put("configMap", Collections.singletonMap("name", "app-conf"));
            Map<String, Object> sparkCustomResourceSpec = (Map<String, Object>)sparkCustomResource.get("spec");
            sparkCustomResourceSpec.put("volumes", Arrays.asList(configMapVolume));

            sparkCustomResource.put("spec", sparkCustomResourceSpec);

            client.customResource(customResourceDefinitionContext).edit("default", customResourceName, sparkCustomResource);
            log("Edition of custom resource successful.");

        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
