package io.fabric8.openshift;

import io.fabric8.openshift.api.model.SubjectAccessReview;
import io.fabric8.openshift.api.model.SubjectAccessReviewBuilder;
import io.fabric8.openshift.api.model.SubjectAccessReviewResponse;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class SubjectAccessReviewExample {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            SubjectAccessReview sar = new SubjectAccessReviewBuilder()
                    .withUser("kubeadmin")
                    .withVerb("get")
                    .withGroups("apps")
                    .withResource("deployments")
                    .withNamespace("rokumar")
                    .build();

            SubjectAccessReviewResponse response = client.subjectAccessReviews().create(sar);
            System.out.println(response.getAllowed());
            System.out.println(response.getReason());
        }
    }
}