package io.fabric8;

import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.util.Collections;

public class ServiceAccountInformerLabels {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            SharedInformerFactory factory = client.informers();

            SharedIndexInformer<ServiceAccount> saInformer = factory.sharedIndexInformerFor(ServiceAccount.class,
                    new OperationContext().withLabels(Collections.singletonMap("foo", "bar")),
                    10 * 60 * 1000);

            saInformer.addEventHandler(new ResourceEventHandler<ServiceAccount>() {
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

            factory.startAllRegisteredInformers();

            Thread.sleep(30 * 160 * 1000L);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
