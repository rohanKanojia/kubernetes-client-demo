package io.fabric8;

import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class CanITest {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            SelfSubjectAccessReview ssar = new SelfSubjectAccessReviewBuilder()
                    .withNewSpec()
                    .withNewResourceAttributes()
                    .withGroup("apps")
                    .withResource("deployments")
                    .withVerb("create")
                    .withNamespace("dev")
                    .endResourceAttributes()
                    .endSpec()
                    .build();

            ssar = client.authorization().v1().selfSubjectAccessReview().create(ssar);

            System.out.println("Allowed: " + ssar.getStatus().getAllowed());
        }
    }
}
