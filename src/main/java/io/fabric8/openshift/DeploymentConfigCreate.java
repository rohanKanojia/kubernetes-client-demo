package io.fabric8.openshift;

import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class DeploymentConfigCreate {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            DeploymentConfig deploymentConfig = client.deploymentConfigs().load(DeploymentConfigCreate.class.getResourceAsStream("/test-dc.yml")).get();
            client.deploymentConfigs().inNamespace("rokumar").createOrReplace(deploymentConfig);
        }
    }
}