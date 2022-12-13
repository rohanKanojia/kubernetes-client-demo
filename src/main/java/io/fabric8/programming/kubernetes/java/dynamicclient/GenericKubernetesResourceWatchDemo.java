package io.fabric8.programming.kubernetes.java.dynamicclient;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericKubernetesResourceWatchDemo {
  private static final Logger logger = LoggerFactory.getLogger(GenericKubernetesResourceWatchDemo.class);

  public static void main(String[] args) throws InterruptedException {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "Info");
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      Watch watch = client.genericKubernetesResources("stable.example.com/v1", "CronTab")
          .inNamespace("default")
          .watch(new Watcher<>() {
            @Override
            public void eventReceived(Action action, GenericKubernetesResource genericKubernetesResource) {
              logger.info("{} {}", action.name(), genericKubernetesResource.getMetadata().getName());
            }

            @Override
            public void onClose(WatcherException e) {
              logger.info("Closing due to {} ", e.getMessage());
            }
          });

      logger.info("Watch open for 30 seconds");
      Thread.sleep(30 * 1000L);
      watch.close();
      logger.info("Watch closed");
    }
  }
}
