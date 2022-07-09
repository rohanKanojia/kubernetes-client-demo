package io.fabric8;

import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;

import java.util.Collections;

public class ServiceAccountInformerLabels {
    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            SharedIndexInformer<ServiceAccount> serviceAccountSharedIndexInformer = client.serviceAccounts().withLabels(Collections.singletonMap("foo", "bar")).inform(new ResourceEventHandler<ServiceAccount>() {
                @Override
                public void onAdd(ServiceAccount serviceAccount) {
                    System.out.printf("ADDED %s/%s\n", serviceAccount.getMetadata().getNamespace(), serviceAccount.getMetadata().getName());
                }

                @Override
                public void onUpdate(ServiceAccount serviceAccount, ServiceAccount t1) {
                    System.out.printf("MODIFIED %s/%s\n", serviceAccount.getMetadata().getNamespace(), serviceAccount.getMetadata().getName());

                }

                @Override
                public void onDelete(ServiceAccount serviceAccount, boolean b) {
                    System.out.printf("DELETED %s/%s\n", serviceAccount.getMetadata().getNamespace(), serviceAccount.getMetadata().getName());
                }
            });

            Thread.sleep(30 * 160 * 1000L);
            serviceAccountSharedIndexInformer.stop();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            interruptedException.printStackTrace();
        }
    }
}
