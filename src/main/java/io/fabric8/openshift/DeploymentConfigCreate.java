package io.fabric8.openshift;

import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class DeploymentConfigCreate {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            DeploymentConfig deploymentConfig = client.deploymentConfigs().load(DeploymentConfigCreate.class.getResourceAsStream("/test-dc.yml")).item();
            client.deploymentConfigs().inNamespace("rokumar").resource(deploymentConfig).serverSideApply();
        }
    }
}