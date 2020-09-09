package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class CertificateSigningRequestDemo {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.load(CertificateSigningRequestDemo.class.getResourceAsStream("/k8s-csr.yml")).createOrReplace();
        }
    }
}