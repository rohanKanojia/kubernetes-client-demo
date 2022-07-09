package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ReadFileAsStream {
    public static void main(String[] args) throws IOException {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            try (InputStream is = client.pods()
                    .inNamespace("default")
                    .withName("quarkus-84dc4885b-tsck6")
                    .inContainer("quarkus")
                    .file("/tmp/jobExample.yml").read()) {
                String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                System.out.println(result);
            }
        }
    }
}