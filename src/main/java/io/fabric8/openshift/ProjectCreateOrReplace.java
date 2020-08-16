package io.fabric8.openshift;

import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProjectCreateOrReplace {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            Project project = new ProjectBuilder()
                    .withNewMetadata().withName("this-is-a-test").endMetadata()
                    .build();

            client.projects().createOrReplace(project);
        }
    }
}
