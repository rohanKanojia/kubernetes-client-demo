package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.util.logging.Logger;

public class InformerDemo {
    private static final Logger logger = Logger.getLogger(InformerDemo.class.getSimpleName());

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            SharedInformerFactory sharedInformerFactory = client.informers();
            SharedIndexInformer<Pod> podInformer = sharedInformerFactory.sharedIndexInformerFor(Pod.class, 30 * 1000L);
            logger.info("Informer factory initialized.");

            podInformer.addEventHandler(
                    new ResourceEventHandler<Pod>() {
                        @Override
                        public void onAdd(Pod pod) {
                            System.out.println("Pod " + pod.getMetadata().getNamespace() + "/" + pod.getMetadata().getName() + " got added");
                        }

                        @Override
                        public void onUpdate(Pod oldPod, Pod newPod) {
                            System.out.println("Pod " + oldPod.getMetadata().getNamespace() + "/" + oldPod.getMetadata().getName() + " got updated");
                        }

                        @Override
                        public void onDelete(Pod pod, boolean deletedFinalStateUnknown) {
                            logger.info("Pod " + pod.getMetadata().getName() + " got deleted");
                        }
                    }
            );

            logger.info("Starting all registered informers");
            sharedInformerFactory.startAllRegisteredInformers();

            // Wait for 1 minute
            Thread.sleep(60 * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
