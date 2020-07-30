package io.fabric8.openshift;

import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.api.model.ProjectRequestBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProjectRequestDemo {
    public static void main(String[] args) {
        try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient()) {
            ProjectRequest projectRequest = new ProjectRequestBuilder()
                    .withNewMetadata().withName("foo-project").endMetadata()
                    .withDescription("This is foo Project")
                    .withDisplayName("Foo")
                    .build();
            openShiftClient.projectrequests().create(projectRequest);

        }
    }
}
