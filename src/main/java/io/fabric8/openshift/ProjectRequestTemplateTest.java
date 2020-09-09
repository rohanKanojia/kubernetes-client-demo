package io.fabric8.openshift;

import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProjectRequestTemplateTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            client.projects().createProjectAndRoleBindings("rokumar", "Rohan Kumar", "rokumar", "developer", "developer");
        }
    }
}
