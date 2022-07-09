package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Collections;
import java.util.Map;

public class StatefulSetRolloutExample {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            updateImage(client);
            restart(client);
            pause(client);
            resume(client);
            undo(client);
            updateImage(client, Collections.singletonMap("nginx", "nginx:perl"));
            System.exit(0);
        }
    }

    private static void updateImage(KubernetesClient client) {
        client.apps().statefulSets()
                .inNamespace("default")
                .withName("web")
                .rolling()
                .updateImage("nginx:1.19");
    }

    private static void updateImage(KubernetesClient client, Map<String, String> params) {
        client.apps().statefulSets()
                .inNamespace("default")
                .withName("web")
                .rolling()
                .updateImage(params);
    }

    private static void restart(KubernetesClient client) {
        client.apps().statefulSets()
                .inNamespace("default")
                .withName("web")
                .rolling()
                .restart();
    }

    private static void pause(KubernetesClient client) {
        client.apps().statefulSets()
                .inNamespace("default")
                .withName("web")
                .rolling()
                .pause();
    }

    private static void resume(KubernetesClient client) {
        client.apps().statefulSets()
                .inNamespace("default")
                .withName("web")
                .rolling()
                .resume();
    }

    private static void undo(KubernetesClient client) {
        client.apps().statefulSets()
                .inNamespace("default")
                .withName("web")
                .rolling()
                .undo();
    }
}
