package io.fabric8;

import io.fabric8.crd.CronTab;
import io.fabric8.crd.CronTabList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.util.logging.Logger;

public class NamespacedInformerForCustomResource {
    private static Logger logger = Logger.getLogger(NamespacedInformerDemo.class.getSimpleName());

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            SharedInformerFactory sharedInformerFactory = client.informers();
            CustomResourceDefinitionContext crdContext = new CustomResourceDefinitionContext.Builder()
                    .withVersion("v1")
                    .withScope("Namespaced")
                    .withGroup("stable.example.com")
                    .withPlural("crontabs")
                    .build();

            SharedIndexInformer<CronTab> informer = sharedInformerFactory.sharedIndexInformerForCustomResource(
                    crdContext,
                    CronTab.class,
                    CronTabList.class,
                    new OperationContext().withNamespace("default"),
                    30 * 1000L);
            logger.info("Informer factory initialized.");

            informer.addEventHandler(
                    new ResourceEventHandler<CronTab>() {
                        @Override
                        public void onAdd(CronTab cronTab) {
                            logger.info("CronTab " + cronTab.getMetadata().getName() + " got added");
                        }

                        @Override
                        public void onUpdate(CronTab oldCronTab, CronTab newCronTab) {
                            logger.info("CronTab " + oldCronTab.getMetadata().getName() + " got updated");
                        }

                        @Override
                        public void onDelete(CronTab cronTab, boolean deletedFinalStateUnknown) {
                            logger.info("CronTab " + cronTab.getMetadata().getName() + " got deleted");
                        }
                    });

            logger.info("Starting all registered informers");
            sharedInformerFactory.startAllRegisteredInformers();

            // Wait for 1 minute
            Thread.sleep(15 * 60 * 1000L);
            sharedInformerFactory.stopAllRegisteredInformers();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
