package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerEventListener;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class InformerDemo {
  private static final Logger logger = LoggerFactory.getLogger(InformerDemo.class.getSimpleName());

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      SharedInformerFactory sharedInformerFactory = client.informers();
      SharedIndexInformer<Pod> podInformer = sharedInformerFactory.sharedIndexInformerFor(Pod.class, 30 * 1000L);
      logger.info("Informer factory initialized.");

      podInformer.addEventHandler(
          new ResourceEventHandler<>() {
            @Override
            public void onAdd(Pod pod) {
              logger.info("Pod {}/{} got added", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
            }

            @Override
            public void onUpdate(Pod oldPod, Pod newPod) {
              logger.info("Pod {}/{} got updated", oldPod.getMetadata().getNamespace(), oldPod.getMetadata().getName());
            }

            @Override
            public void onDelete(Pod pod, boolean deletedFinalStateUnknown) {
              logger.info("Pod {}/{} got deleted", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
            }
          }
      );

      logger.info("Starting all registered informers");
      Future<Void> startAllInformersFuture = sharedInformerFactory.startAllRegisteredInformers();
      startAllInformersFuture.get();

      // Wait for 1 minute
      Thread.sleep(60 * 1000L);
      sharedInformerFactory.stopAllRegisteredInformers();
    } catch (ExecutionException executionException) {
      logger.error("Error in starting all informers", executionException);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn("interrupted ", e);
    }
  }
}
