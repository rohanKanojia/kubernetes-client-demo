package io.fabric8;

//import io.fabric8.kubernetes.api.model.authorization.v1.LocalSubjectAccessReview;
//import io.fabric8.kubernetes.api.model.authorization.v1.LocalSubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class LocalSubjectAccessReviewExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
//            LocalSubjectAccessReview lsar = new LocalSubjectAccessReviewBuilder()
//                    .withNewMetadata().withNamespace("default").endMetadata()
//                    .withNewSpec()
//                    .withUser("foo")
//                    .withNewResourceAttributes()
//                    .withNamespace("default")
//                    .withVerb("get")
//                    .withGroup("apps")
//                    .withResource("pods")
//                    .endResourceAttributes()
//                    .endSpec()
//                    .build();
//             lsar = client.authorization().v1().localSubjectAccessReview().inNamespace("default").create(lsar);
//             System.out.println(lsar.getStatus().getAllowed());
        }
    }
}