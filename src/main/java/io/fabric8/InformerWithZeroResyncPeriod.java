package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.util.logging.Logger;

public class InformerWithZeroResyncPeriod {
    private static final Logger logger = Logger.getLogger(InformerWithZeroResyncPeriod.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            SharedInformerFactory sharedInformerFactory = client.informers();
            SharedIndexInformer<Pod> podInformer = sharedInformerFactory.sharedIndexInformerFor(Pod.class, PodList.class, 0);
            logger.info("Informer factory initialized.");

            podInformer.addEventHandler(
                    new ResourceEventHandler<Pod>() {
                        @Override
                        public void onAdd(Pod pod) {
                            logger.info("Pod " + pod.getMetadata().getName() + " got added");
                        }

                        @Override
                        public void onUpdate(Pod oldPod, Pod newPod) {
                            logger.info("Pod " + oldPod.getMetadata().getName() + " got updated");
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
        }
    }
}
