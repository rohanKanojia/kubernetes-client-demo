package io.fabric8;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionFluent;
import io.fabric8.kubernetes.api.model.apiextensions.JSONSchemaProps;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomResourceSchemaValidation {
    private static final Logger logger = Logger.getLogger(CustomResourceSchemaValidation.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        final URL resource = CustomResourceSchemaValidation.class.getResource("/test.json");

        final JSONSchemaProps jsonSchemaProps = mapper.readValue(resource, JSONSchemaProps.class);

        final CustomResourceDefinitionFluent.SpecNested<CustomResourceDefinitionBuilder> crdBuilder = new CustomResourceDefinitionBuilder()
                .withApiVersion("apiextensions.k8s.io/v1beta1")
                .withNewMetadata()
                .withName("somethings.example.com")
                .endMetadata()
                .withNewSpec()
                .withNewNames()
                .withKind("Example")
                .withPlural("somethings")
                .withShortNames(Arrays.asList("ex")).endNames()
                .withGroup("example.com")
                .withVersion("v1")
                .withScope("Namespaced");
        final CustomResourceDefinition customResourceDefinition = crdBuilder
                .withNewValidation()
                .withOpenAPIV3Schema(jsonSchemaProps)
                .endValidation()
                .endSpec()
                .build();

        KubernetesClient client = new DefaultKubernetesClient();
//        // To make this test idempotent
//        client.customResourceDefinitions().delete(customResourceDefinition);
//        do {
//            System.out.println("Waiting until CRD is gone from K8s");
//            Thread.sleep(500);
//        } while (client.customResourceDefinitions().list()
//                .getItems()
//                .stream()
//                .anyMatch(crd -> crd.getMetadata().getName().contains("somethings")));

        client.customResourceDefinitions().createOrReplace(customResourceDefinition);
        logger.log(Level.INFO, "CRD created once");
        // This will always fail
        client.customResourceDefinitions().createOrReplace(customResourceDefinition);
        logger.log(Level.INFO, "CRD created twice");
    }

    private static void log(String action) {
        logger.info(action);
    }
}
