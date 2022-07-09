package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CascadeDeletionDemo {
    private static final Logger logger = Logger.getLogger(CascadeDeletionDemo.class
            .getName());

    public static void main(String args[]) {

        try {
            logger.log(Level.INFO, "Creating Kubernetes client");
            KubernetesClient client = new KubernetesClientBuilder().build();
            logger.log(Level.INFO, "Kubernetes client successfully created");

            // Run command kubectl run nginx --image=nginx --replicas=1
            client.apps().deployments().inNamespace("default").withName("nginx").delete();

            client.close();
        } catch (KubernetesClientException aException) {
            logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
            aException.printStackTrace();
        }
    }
}
