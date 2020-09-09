package io.fabric8.openshift;

import io.fabric8.openshift.api.model.LocalSubjectAccessReview;
import io.fabric8.openshift.api.model.LocalSubjectAccessReviewBuilder;
import io.fabric8.openshift.api.model.SubjectAccessReviewResponse;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class LocalSubjectAccessReviewExample {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            LocalSubjectAccessReview lsar = new LocalSubjectAccessReviewBuilder()
                    .withNamespace("rokumar")
                    .withUser("kubeadmin")
                    .withGroups("apps")
                    .withVerb("get")
                    .withResource("Deployment")
                    .build();

            SubjectAccessReviewResponse response = client.localSubjectAccessReviews().inNamespace("rokumar").create(lsar);
            System.out.println(response.getAllowed());
            System.out.println(response.getReason());
        }
    }
}
