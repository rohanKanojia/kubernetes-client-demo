package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.File;

public class UploadFileToPod {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            File fileToUpload = new File("/home/rohaan/work/k8-resource-yamls/jobExample.yml");
            client.pods().inNamespace("default")         // <- Namespace of Pod
                    .withName("quarkus-84dc4885b-tsck6") // <- Name of Pod
                    .file("/tmp/jobExample.yml")         // <- Target location of copied file inside Pod
                    .upload(fileToUpload.toPath());        // <- Path of local file
        }
    }
}