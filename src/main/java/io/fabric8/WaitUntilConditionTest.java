package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.concurrent.TimeUnit;

public class WaitUntilConditionTest {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.load(WaitUntilConditionTest.class.getResourceAsStream("/nginx-deployment.yml"))
                    .inNamespace("default")
                    .createOrReplace();

            client.apps().deployments().inNamespace("default").withName("nginx-deployment")
                    .waitUntilCondition(d -> d.getStatus().getReadyReplicas() != null && d.getStatus().getReadyReplicas() == 2, 10, TimeUnit.SECONDS);
        }
    }
}