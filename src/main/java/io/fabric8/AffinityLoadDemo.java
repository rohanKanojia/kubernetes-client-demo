package io.fabric8;

import io.fabric8.kubernetes.api.model.Affinity;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.utils.Serialization;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AffinityLoadDemo {
    private static final Logger logger = Logger.getLogger(AffinityLoadDemo.class
            .getName());

    public static void main(String[] args) {

        try {
            Affinity affinity = Serialization
                    .unmarshal(AffinityLoadDemo.class.getResourceAsStream("/affinity.yml"), Affinity.class);
            System.out.println(affinity.getNodeAffinity());
        } catch (KubernetesClientException aException) {
            logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
            aException.printStackTrace();
        }
    }
}
