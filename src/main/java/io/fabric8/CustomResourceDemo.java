package io.fabric8;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CustomResourceDemo {
    private static final Logger logger = Logger.getLogger(CustomResourceDemo.class.getName());

    public static void main(String args[]) throws IOException {
        String customResourceDefinitionPath = "/home/Rohaan/go/src/github.com/rohanKanojia/hello-operator/deploy/crds/test_v1alpha1_hello_crd.yaml";
        String customResourcePath = "/home/Rohaan/go/src/github.com/rohanKanojia/hello-operator/deploy/crds/test_v1alpha1_hello_cr.yaml";


        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            CustomResourceDefinition crd = client.customResourceDefinitions().load(customResourceDefinitionPath).get();
            client.customResourceDefinitions().create(crd);

            CustomResourceDefinitionContext myCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withGroup("test.fabric8.io")
                    .withScope("Namespaced")
                    .withVersion("v1alpha1")
                    .withPlural("hellos")
                    .build();

            client.customResource(myCrdContext).create("default", new FileInputStream(customResourcePath));
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
