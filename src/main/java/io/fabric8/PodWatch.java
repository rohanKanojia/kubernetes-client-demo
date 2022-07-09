package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class PodWatch {
    private static final Logger logger = LoggerFactory.getLogger(PodWatch.class.getSimpleName());

    public static void main(String[] args) {
        String namespace = "default";

        // Latch for Watch termination
        final CountDownLatch isWatchClosed = new CountDownLatch(1);
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.pods().inNamespace(namespace).watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {
                    logger.info("{} {}", action.name(), pod.getMetadata().getName());
                    switch (action.name()) {
                        case "ADDED":
                            logger.info("{}/{} got added", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
                            break;
                        case "DELETED":
                            logger.info("{}/{} got deleted", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
                            break;
                        case "MODIFIED":
                            logger.info("{}/{} got modified", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
                            break;
                        default:
                            logger.error("Unrecognized event: {}", action.name());
                    }
                }

                @Override
                public void onClose() {
                    logger.info("Watch closed");
                    isWatchClosed.countDown();
                }

                @Override
                public void onClose(WatcherException e) {
                    logger.info("Watched closed due to exception ", e);
                    isWatchClosed.countDown();
                }
            });

            // Wait till watch gets closed
            isWatchClosed.await();
        } catch (InterruptedException interruptedException) {
            logger.info( "Thread Interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}
