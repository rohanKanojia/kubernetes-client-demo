package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.DeletionPropagation;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class DeploymentConfigDelete {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            client.deploymentConfigs().inNamespace("rokumar").withName("frontend").delete();
            client.deploymentConfigs().inNamespace("rokumar").withName("eclipse-jkube-sample-spring-boot-jib").delete();
            client.deploymentConfigs().inNamespace("rokumar").withName("wordpress-mysql-example").withPropagationPolicy(DeletionPropagation.FOREGROUND).withGracePeriod(0l).delete();
        }
    }
}