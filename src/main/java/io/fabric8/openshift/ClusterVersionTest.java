package io.fabric8.openshift;

import io.fabric8.openshift.api.model.ClusterVersion;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ClusterVersionTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            System.out.println(client.getOpenshiftUrl().toString());
            ClusterVersion clusterVersion = client.config().clusterVersions().withName("version").get();
            System.out.println("Cluster Version: " + clusterVersion.getStatus().getDesired().getVersion());
        }
    }
}
