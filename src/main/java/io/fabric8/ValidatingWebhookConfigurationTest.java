package io.fabric8;

import io.fabric8.kubernetes.api.model.admissionregistration.v1.ValidatingWebhookConfiguration;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.ValidatingWebhookConfigurationBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ValidatingWebhookConfigurationTest {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            ValidatingWebhookConfiguration vwc = new ValidatingWebhookConfigurationBuilder()
                    .withNewMetadata().withName("pod-policy.example.com").endMetadata()
                    .addNewWebhook()
                    .withName("pod-policy.example.com")
                    .addNewRule()
                    .withApiGroups("")
                    .withApiVersions("v1")
                    .withOperations("CREATE")
                    .withResources("pods")
                    .withScope("Namespaced")
                    .endRule()
                    .withNewClientConfig()
                    .withNewService()
                    .withNamespace("example-namespace")
                    .withName("example-service")
                    .endService()
                    .endClientConfig()
                    .withAdmissionReviewVersions("v1", "v1beta1")
                    .withSideEffects("None")
                    .withTimeoutSeconds(5)
                    .endWebhook()
                    .build();

            client.admissionRegistration().v1().validatingWebhookConfigurations().resource(vwc).create();
        }
    }
}
