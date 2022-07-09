package io.fabric8.openshift;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProjectRequestTemplateTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            client.projects().createProjectAndRoleBindings("rokumar", "Rohan Kumar", "rokumar", "developer", "developer");
        }
    }
}
