package io.fabric8;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class WaitUntilReadyForLists {
    private static final Logger logger = Logger.getLogger(WaitUntilReadyForLists.class
            .getName());

    public static void main(String[] args) throws InterruptedException {
        try (final KubernetesClient client = new KubernetesClientBuilder().build()) {
            String namespace = "default";
            Pod pod = new PodBuilder()
                    .withNewMetadata().withName("p2").withLabels(Collections.singletonMap("app", "p2")).endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("myapp-container")
                    .withImage("busybox:1.28")
                    .withCommand("sh", "-c", "echo The app is running!; sleep 10")
                    .endContainer()
                    .addNewInitContainer()
                    .withName("init-myservice")
                    .withImage("busybox:1.28")
                    .withCommand("sh", "-c", "echo inititalizing...; sleep 5")
                    .endInitContainer()
                    .endSpec()
                    .build();

            Pod secondPod = new PodBuilder()
                    .withNewMetadata().withName("p1").endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("myapp2-container")
                    .withImage("busybox:1.28")
                    .withCommand("sh", "-c", "echo The app is running!; sleep 10")
                    .endContainer()
                    .addNewInitContainer()
                    .withName("init2-mypod")
                    .withImage("busybox:1.28")
                    .withCommand("sh", "-c", "echo initializing...; sleep 5")
                    .endInitContainer()
                    .endSpec()
                    .build();

            Secret mySecret = new SecretBuilder()
                    .withNewMetadata().withName("mysecret").endMetadata()
                    .withType("Opaque")
                    .addToData("username", "YWRtaW4=")
                    .addToData("password", "MWYyZDFlMmU2N2Rm")
                    .build();

            logger.log(Level.INFO, "creating resources");
            pod = client.pods().inNamespace(namespace).resource(pod).create();
            secondPod = client.pods().inNamespace(namespace).resource(secondPod).create();
            mySecret = client.secrets().inNamespace(namespace).resource(mySecret).create();

            logger.log(Level.INFO, "Waiting for resources until they become ready.");
            // For waiting for single resource use this.
            client.resourceList(new KubernetesListBuilder().withItems(pod, secondPod, mySecret).build()).inNamespace(namespace).waitUntilReady(60, TimeUnit.SECONDS);
            logger.log(Level.INFO, "Resources Ready - OK");

            logger.log(Level.INFO, "waiting....");
            Thread.sleep(15 * 1000L);

            logger.log(Level.INFO, "Cleaning up now...");
            // Cleanup
            client.pods().inNamespace(namespace).withName("p2").delete();
            client.pods().inNamespace(namespace).withName("p1").delete();
            client.secrets().inNamespace(namespace).withName("mysecret").delete();
        }
    }
}
