package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class SelfSubjectAccessReviewExample {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            SelfSubjectAccessReview ssar = new SelfSubjectAccessReviewBuilder()
                    .withNewSpec()
                    .withNewResourceAttributes()
                    .withGroup("apps")
                    .withResource("deployments")
                    .withVerb("create")
                    .withNamespace("rokumar")
                    .endResourceAttributes()
                    .endSpec()
                    .build();

            ssar = client.authorization().v1().selfSubjectAccessReview().create(ssar);
        }
    }
}