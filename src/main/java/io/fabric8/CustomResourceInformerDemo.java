package io.fabric8;

import io.fabric8.crd.CronTab;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.util.logging.Logger;

public class CustomResourceInformerDemo {
    private static final Logger logger = Logger.getLogger(CustomResourceInformerDemo.class.getSimpleName());

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            SharedInformerFactory sharedInformerFactory = client.informers();
            SharedIndexInformer<CronTab> podInformer = sharedInformerFactory.sharedIndexInformerForCustomResource(
                    CronTab.class,
                    60 * 1000L);
            logger.info("Informer factory initialized.");

            podInformer.addEventHandler(
                    new ResourceEventHandler<CronTab>() {
                        @Override
                        public void onAdd(CronTab cronTab) {
                            logger.info(cronTab.getMetadata().getName() + " crontab added");
                        }

                        @Override
                        public void onUpdate(CronTab oldCronTab, CronTab newCronTab) {
                            logger.info(oldCronTab.getMetadata().getName() + " crontab updated");
                        }

                        @Override
                        public void onDelete(CronTab cronTab, boolean deletedFinalStateUnknown) {
                            logger.info(cronTab.getMetadata().getName() + " crontab deleted");
                        }
                    });

            logger.info("Starting all registered informers");
            sharedInformerFactory.startAllRegisteredInformers();

            // Wait for 1 minute
            Thread.sleep(60 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
