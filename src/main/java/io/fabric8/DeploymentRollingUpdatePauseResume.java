package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.concurrent.TimeUnit;

public class DeploymentRollingUpdatePauseResume {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.apps().deployments()
                    .inNamespace("default")
                    .withName("hello-dep")
                    .rolling()
                    .pause();

            System.out.print("Pausing Rollout...");
            TimeUnit.MINUTES.sleep(1);
            System.out.println("Resuming Rollout");

            client.apps().deployments()
                    .inNamespace("default")
                    .withName("hello-dep")
                    .rolling()
                    .resume();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
