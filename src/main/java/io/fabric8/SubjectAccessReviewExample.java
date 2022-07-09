package io.fabric8;

import io.fabric8.kubernetes.api.model.authorization.v1.SubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.v1.SubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class SubjectAccessReviewExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            SubjectAccessReview sar = new SubjectAccessReviewBuilder()
                    .withNewSpec()
                    .withNewResourceAttributes()
                    .withGroup("apps")
                    .withResource("deployments")
                    .withVerb("create")
                    .withNamespace("default")
                    .endResourceAttributes()
                    .withUser("kubeadmin")
                    .endSpec()
                    .build();

            sar = client.authorization().v1().subjectAccessReview().create(sar);

            System.out.println("Allowed: "+  sar.getStatus().getAllowed());
        }
    }
}
