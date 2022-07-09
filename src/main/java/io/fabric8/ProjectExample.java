package io.fabric8;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectExample {

    private static final Logger logger = Logger.getLogger(ProjectExample.class
            .getName());

    public static void main(String args[]) {

        try {
            KubernetesClient client = new KubernetesClientBuilder().build();

            NamespaceList namespaceList = client.namespaces().withLabels(Collections.singletonMap("name", "rohan")).list();

            logger.log(Level.INFO, "Got " + namespaceList.getItems().size() + " items.");

            for (Namespace ns : namespaceList.getItems()) {
                logger.log(Level.INFO, ns.getMetadata().getName());
            }

        } catch (KubernetesClientException aException) {
            logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
            aException.printStackTrace();
        }
    }
}
