package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.File;
import java.nio.file.Path;

public class DownloadFileFromPod {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            // Path Where to copy file to local storage
            Path downloadToPath = new File("/home/rohaan/Downloads/quarkus-1.0.0-runner.jar").toPath();
            // Using Kubernetes Client to copy file from pod
            client.pods()
                    .inNamespace("default")                         // <- Namespace of pod
                    .withName("quarkus-84dc4885b-tsck6")            // <- Name of pod
                    .file("/deployments/quarkus-1.0.0-runner.jar")  // <- Path of file inside pod
                    .copy(downloadToPath);                            // <- Local path where to copy downloaded file
        }
    }
}
