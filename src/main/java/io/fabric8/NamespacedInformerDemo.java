package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NamespacedInformerDemo {
    private static final Logger logger = LoggerFactory.getLogger(NamespacedInformerDemo.class.getSimpleName());

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            logger.info("Creating SharedIndexInformer for default namespace");
            SharedIndexInformer<Pod> podInformer = client.pods().inNamespace("default").inform(new ResourceEventHandler<Pod>() {
                @Override
                public void onAdd(Pod pod) {
                    logger.info("Pod {} got added", pod.getMetadata().getName());
                }

                @Override
                public void onUpdate(Pod oldPod, Pod newPod) {
                    logger.info("Pod {} got updated", oldPod.getMetadata().getName());
                }

                @Override
                public void onDelete(Pod pod, boolean deletedFinalStateUnknown) {
                    logger.info("Pod {} got deleted", pod.getMetadata().getName());
                }
            });

            // Wait for 1 minute
            Thread.sleep(60 * 1000L);
            logger.info("Stopping Pod SharedIndexInformer");
            podInformer.close();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("Interrupted ", e);
        }
    }
}
