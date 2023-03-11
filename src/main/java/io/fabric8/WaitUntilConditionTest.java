package io.fabric8;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.concurrent.TimeUnit;

public class WaitUntilConditionTest {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            Deployment deployment = client.apps().deployments()
                .load(WaitUntilConditionTest.class.getResourceAsStream("/nginx-deployment.yml"))
                .item();
            client.apps().deployments().inNamespace("default").resource(deployment).serverSideApply();

            client.apps().deployments().inNamespace("default").withName("nginx-deployment")
                    .waitUntilCondition(d -> d.getStatus().getReadyReplicas() != null && d.getStatus().getReadyReplicas() == 2, 10, TimeUnit.SECONDS);
        }
    }
}