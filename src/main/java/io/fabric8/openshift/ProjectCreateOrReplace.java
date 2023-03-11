package io.fabric8.openshift;

import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProjectCreateOrReplace {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            Project project = new ProjectBuilder()
                    .withNewMetadata().withName("this-is-a-test").endMetadata()
                    .build();

            client.projects().resource(project).serverSideApply();
        }
    }
}
