package io.fabric8;

import io.fabric8.crd.mode.v1.CronTab;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class CustomResourceInformerDemo {
  private static final Logger logger = LoggerFactory.getLogger(CustomResourceInformerDemo.class.getSimpleName());

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      SharedInformerFactory sharedInformerFactory = client.informers();
      SharedIndexInformer<CronTab> cronTabInformer = sharedInformerFactory.sharedIndexInformerFor(
          CronTab.class,
          60 * 1000L);
      logger.info("Informer factory initialized.");

      cronTabInformer.addEventHandler(
          new ResourceEventHandler<>() {
            @Override
            public void onAdd(CronTab cronTab) {
              logger.info("{}/{} crontab added", cronTab.getMetadata().getNamespace(), cronTab.getMetadata().getName());
            }

            @Override
            public void onUpdate(CronTab oldCronTab, CronTab newCronTab) {
              logger.info("{}/{} crontab updated", oldCronTab.getMetadata().getNamespace(), oldCronTab.getMetadata().getName());
            }

            @Override
            public void onDelete(CronTab cronTab, boolean deletedFinalStateUnknown) {
              logger.info("{}/{} crontab deleted", cronTab.getMetadata().getNamespace(), cronTab.getMetadata().getName());
            }
          });

      logger.info("Starting all registered informers");
      Future<Void> startAllInformersFuture = sharedInformerFactory.startAllRegisteredInformers();
      startAllInformersFuture.get();

      // Wait for 1 minute
      Thread.sleep(60 * 1000L);
      sharedInformerFactory.stopAllRegisteredInformers();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn("interrupted ", e);
    } catch (ExecutionException executionException) {
      logger.error("Error in starting all informers", executionException);
    }
  }
}
