package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class CertificateSigningRequestDemo {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.load(CertificateSigningRequestDemo.class.getResourceAsStream("/k8s-csr.yml")).create();
        }
    }
}