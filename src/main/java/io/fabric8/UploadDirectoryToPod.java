package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.File;

public class UploadDirectoryToPod {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            File fileToUpload = new File("/home/rohaan/work/repos/test-dir");
            client.pods().inNamespace("default")  // <- Namespace of Pod
                    .withName("frontend-6zzh4")   // <- Name of Pod
                    .dir("/tmp/test-dir")         // <- Path of directory inside Pod
                    .upload(fileToUpload.toPath()); // <- Local Path of directory
        }
    }
}