package io.fabric8.openshift;

import io.fabric8.openshift.api.model.ClusterVersion;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class ClusterVersionTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            System.out.println(client.getOpenshiftUrl().toString());
            ClusterVersion clusterVersion = client.config().clusterVersions().withName("version").get();
            System.out.println("Cluster Version: " + clusterVersion.getStatus().getDesired().getVersion());
        }
    }
}
