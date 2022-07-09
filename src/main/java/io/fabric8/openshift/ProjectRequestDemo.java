package io.fabric8.openshift;

import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.api.model.ProjectRequestBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class ProjectRequestDemo {
    public static void main(String[] args) {
        try (OpenShiftClient openShiftClient = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            ProjectRequest projectRequest = new ProjectRequestBuilder()
                    .withNewMetadata().withName("foo-project").endMetadata()
                    .withDescription("This is foo Project")
                    .withDisplayName("Foo")
                    .build();
            openShiftClient.projectrequests().create(projectRequest);

        }
    }
}
