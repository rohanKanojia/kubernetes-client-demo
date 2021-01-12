package io.fabric8;

import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.IOException;
import java.util.logging.Logger;

public class CustomResourceDemo {
    private static final Logger logger = Logger.getLogger(CustomResourceDemo.class.getName());

    public static void main(String[] args) throws IOException {

        Config config = new ConfigBuilder()
                .withMasterUrl("https://192.168.99.100:8443")
                .build();
        try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
            CustomResourceDefinition crd = client.apiextensions().v1beta1().customResourceDefinitions().load(CustomResourceDemo.class.getResourceAsStream("/hello-crd.yml")).get();
            client.apiextensions().v1beta1().customResourceDefinitions().create(crd);
            log("Created Custom Resource Definition");

            CustomResourceDefinitionContext myCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withGroup("test.fabric8.io")
                    .withScope("Namespaced")
                    .withVersion("v1alpha1")
                    .withPlural("hellos")
                    .build();

            client.customResource(myCrdContext).create("default", CustomResourceDemo.class.getResourceAsStream("/hello-cr.yml"));
            log("Successfully created Custom Resource");
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
