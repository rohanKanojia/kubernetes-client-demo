package io.fabric8;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinitionFluent;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.JSONSchemaProps;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomResourceSchemaValidation {
    private static final Logger logger = Logger.getLogger(CustomResourceSchemaValidation.class.getName());

    public static void main(String[] args) throws IOException {
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
                .withShortNames(Collections.singletonList("ex")).endNames()
                .withGroup("example.com")
                .withVersion("v1")
                .withScope("Namespaced");
        final CustomResourceDefinition customResourceDefinition = crdBuilder
                .withNewValidation()
                .withOpenAPIV3Schema(jsonSchemaProps)
                .endValidation()
                .endSpec()
                .build();

        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.apiextensions().v1beta1().customResourceDefinitions().createOrReplace(customResourceDefinition);
            logger.log(Level.INFO, "CRD created once");
            // This will always fail
            client.apiextensions().v1beta1().customResourceDefinitions().createOrReplace(customResourceDefinition);
            logger.log(Level.INFO, "CRD created twice");
        }
//        // To make this test idempotent
//        client.apiextensions().v1beta1().customResourceDefinitions().delete(customResourceDefinition);
//        do {
//            System.out.println("Waiting until CRD is gone from K8s");
//            Thread.sleep(500);
//        } while (client.apiextensions().v1beta1().customResourceDefinitions().list()
//                .getItems()
//                .stream()
//                .anyMatch(io.fabric8.crd -> io.fabric8.crd.getMetadata().getName().contains("somethings")));

    }
}
