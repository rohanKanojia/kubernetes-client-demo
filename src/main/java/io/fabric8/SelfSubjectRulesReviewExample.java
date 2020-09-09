package io.fabric8;

import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectRulesReview;
import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectRulesReviewBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class SelfSubjectRulesReviewExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            SelfSubjectRulesReview selfSubjectRulesReview = new SelfSubjectRulesReviewBuilder()
                    .withNewMetadata().withName("foo").endMetadata()
                    .withNewSpec()
                    .withNamespace("default")
                    .endSpec()
                    .build();

            selfSubjectRulesReview = client.authorization().v1().selfSubjectRulesReview().create(selfSubjectRulesReview);
            System.out.println(selfSubjectRulesReview.getStatus().getIncomplete());
            System.out.println("non resource rules: " + selfSubjectRulesReview.getStatus().getNonResourceRules().size());
            System.out.println("resource rules: " + selfSubjectRulesReview.getStatus().getResourceRules().size());
        }
    }
}