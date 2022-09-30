package io.fabric8;

import io.fabric8.crd.mode.v1.CronTab;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;

import java.util.logging.Logger;

public class NamespacedInformerForCustomResource {
    private static final Logger logger = Logger.getLogger(NamespacedInformerDemo.class.getSimpleName());

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            SharedIndexInformer<CronTab> informer = client.resources(CronTab.class).inNamespace("default").inform(
                new ResourceEventHandler<>() {
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
                },30 * 1000L);
            logger.info("Informer initialized.");

            // Wait for 1 minute
            Thread.sleep(15 * 60 * 1000L);
            informer.stop();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
