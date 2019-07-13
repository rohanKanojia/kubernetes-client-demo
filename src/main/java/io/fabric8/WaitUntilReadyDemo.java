package io.fabric8;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.LogWatch;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.rmi.server.LogStream.log;

public class WaitUntilReadyDemo {
    private static final Logger logger = Logger.getLogger(WaitUntilReadyDemo.class
            .getName());

    public static void main(String args[]) throws IOException, InterruptedException {
        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            Pod pod = new PodBuilder()
                    .withNewMetadata().withName("myapp-pod").withLabels(Collections.singletonMap("app", "myapp-pod")).endMetadata()
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

            String namespace = "default";
            pod = client.pods().inNamespace(namespace).create(pod);
            log("Pod created, waiting for it to get ready");
            client.resource(pod).inNamespace(namespace).waitUntilReady(10, TimeUnit.SECONDS);
            log("Pod is ready now.");

            LogWatch watch = client.pods().inNamespace(namespace).withName(pod.getMetadata().getName()).watchLog(System.out);
            watch.wait(10);
        }
    }
}
