package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectRulesReview;
import io.fabric8.kubernetes.api.model.authorization.v1.SelfSubjectRulesReviewBuilder;
import io.fabric8.kubernetes.api.model.authorization.v1.SubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.v1.SubjectAccessReviewBuilder;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.Role;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.openshift.api.model.LocalSubjectAccessReview;
import io.fabric8.openshift.api.model.LocalSubjectAccessReviewBuilder;
import io.fabric8.openshift.api.model.SubjectAccessReviewResponse;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class K8sCreateClusterRoleBinding {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            ClusterRoleBinding clusterRoleBinding = client.rbac().clusterRoleBindings()
                    .load(K8sCreateClusterRoleBinding.class.getResourceAsStream("/test-clusterrolebinding.yml"))
                    .get();
            client.rbac().clusterRoleBindings().resource(clusterRoleBinding).create();

            client.pods().inNamespace("rokumar-dev").list();
            Role role = client.rbac().roles().load(K8sCreateClusterRoleBinding.class.getResourceAsStream("/test-role.yml"))
                    .get();
            client.rbac().roles().inNamespace("rokumar-dev").resource(role).create();

            String name = "create-oc-sar";
            io.fabric8.openshift.api.model.SubjectAccessReview sar = new io.fabric8.openshift.api.model.SubjectAccessReviewBuilder()
                    .withResource("pod")
                    .withVerb("get")
                    .build();

            // When
            SubjectAccessReviewResponse sarResponse = client.subjectAccessReviews().create(sar);
            System.out.println(Serialization.asJson(sarResponse));
            System.out.println(Serialization.asJson(client.currentUser()));

            // Given
            SelfSubjectRulesReview ssrr = new SelfSubjectRulesReviewBuilder()
                    .withNewSpec()
                    .withNamespace("default")
                    .endSpec()
                    .build();

            // When
            SelfSubjectRulesReview createdSsrr = client.authorization().v1().selfSubjectRulesReview().create(ssrr);
            System.out.println(Serialization.asJson(createdSsrr));

            // Given
            SubjectAccessReview sar1 = new SubjectAccessReviewBuilder()
                    .withNewSpec()
                    .withNewResourceAttributes()
                    .withNamespace("default")
                    .withVerb("get")
                    .withResource("pods")
                    .endResourceAttributes()
                    .withUser(client.currentUser().getMetadata().getName())
                    .endSpec()
                    .build();

            // When
            SubjectAccessReview createSar = client.authorization().v1().subjectAccessReview().create(sar1);
            System.out.println(Serialization.asJson(createSar));

            // Given
            LocalSubjectAccessReview localSubjectAccessReview = new LocalSubjectAccessReviewBuilder()
                    .withNamespace("default")
                    .withVerb("get")
                    .withResource("pods")
                    .build();

            // When
            SubjectAccessReviewResponse response = client.localSubjectAccessReviews().inNamespace("default").create(localSubjectAccessReview);
            System.out.println(Serialization.asJson(response));
        }
    }
}
