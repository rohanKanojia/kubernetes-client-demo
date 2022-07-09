package io.fabric8;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WatchCustomResourceDemo {
    private static final Logger logger = LoggerFactory.getLogger(WatchCustomResourceDemo.class);

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            String namespace = "default";
            final CountDownLatch closeLatch = new CountDownLatch(1);
            CustomResourceDefinitionContext crdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("dummies.demo.fabric8.io")
                    .withScope("Namespaced")
                    .withVersion("v1")
                    .withPlural("dummies")
                    .withGroup("demo.fabric8.io")
                    .build();

            // Watching custom resources now
            logger.info("Watching custom resources now");
            client.genericKubernetesResources(crdContext).inNamespace(namespace).watch(new Watcher<>() {
                @Override
                public void eventReceived(Action action, GenericKubernetesResource resource) {
                    try {
                        logger.info(action + " : " + resource.getMetadata().getName());
                    } catch (JSONException exception) {
                        logger.error("failed to parse object");
                    }
                }

                @Override
                public void onClose() { }

                @Override
                public void onClose(WatcherException e) {
                    logger.info("Watcher onClose");
                    closeLatch.countDown();
                    if (e != null) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
            closeLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException exception) {
            logger.info("Interrupted: " + exception.getMessage());
        }
    }
}
