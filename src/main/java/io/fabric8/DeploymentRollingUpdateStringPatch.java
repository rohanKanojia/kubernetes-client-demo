package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;

public class DeploymentRollingUpdateStringPatch {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            PatchContext patchContext = new PatchContext.Builder()
                    .withPatchType(PatchType.JSON)
                    .build();

            client.apps().deployments()
                    .inNamespace("default")
                    .withName("hello-dep")
                    .patch(patchContext, "[" +
                            "{\"op\": \"replace\"," +
                            "\"path\":\"/spec/template/spec/containers/0/image\"," +
                            "\"value\":\"gcr.io/google-samples/hello-app:2.0\"}" +
                            "]");

        }
    }
}
