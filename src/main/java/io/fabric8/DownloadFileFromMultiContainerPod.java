package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.File;
import java.nio.file.Path;

public class DownloadFileFromMultiContainerPod {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Path Where to copy file to local storage
            Path downloadToPath = new File("/home/rohaan/Downloads/docker-entrypoint.sh").toPath();
            // Using Kubernetes Client to copy file from pod
            client.pods()
                .inNamespace("default")             // <- Namespace of pod
                .withName("multi-container-pod")    // <- Name of pod
                .inContainer("c1")                  // <- Container from which file has to be downloaded
                .file("/docker-entrypoint.sh")      // <- Path of file inside pod
                .copy(downloadToPath);                // <- Local path where to copy downloaded file
        }
    }
}