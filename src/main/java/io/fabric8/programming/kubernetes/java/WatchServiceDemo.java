package io.fabric8.programming.kubernetes.java;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchServiceDemo {
  private static final Logger logger = LoggerFactory.getLogger(WatchServiceDemo.class.getSimpleName());

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      Watch watch = client.services()
          .inNamespace("default")
          .watch(new Watcher<>() {
            @Override
            public void eventReceived(Action action, Service service) {
              logger.info("{} {}", action.name(), service.getMetadata().getName());
            }

            @Override
            public void onClose(WatcherException e) {
              logger.info("Watch closing {}", e.getMessage());
            }
          });

      Thread.sleep(10 * 1000L);

      watch.close();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
