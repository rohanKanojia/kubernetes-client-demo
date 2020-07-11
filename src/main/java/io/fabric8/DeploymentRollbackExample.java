package io.fabric8;

import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.api.model.extensions.DeploymentRollback;
import io.fabric8.kubernetes.api.model.extensions.DeploymentRollbackBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.internal.SerializationUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DeploymentRollbackExample {
    private static final Logger logger = Logger.getLogger(DeploymentRollbackExample.class
            .getName());

    public static void main(String args[]) {
        try {
            KubernetesClient client = new DefaultKubernetesClient();
            Status status = client.apps().deployments().inNamespace("rokumar")
                    .withName("nginx-deployment")
                    .rollback(new DeploymentRollbackBuilder()
                            .withName("nginx-deployment")
                            .withNewRollbackTo(2l).build());
            logger.log(Level.INFO, status.toString());
            client.close();
        } catch (KubernetesClientException aException) {
            logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
            aException.printStackTrace();
        }
    }
}
