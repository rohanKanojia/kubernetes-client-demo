package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PodWatch {
    private static Logger logger = Logger.getLogger(PodWatch.class.getSimpleName());

    public static void main(String[] args) {
        String namespace = "default";

        // Latch for Watch termination
        final CountDownLatch isWatchClosed = new CountDownLatch(1);
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.pods().inNamespace(namespace).watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {
                    logger.log(Level.INFO, action.name() + " " + pod.getMetadata().getName());
                    switch (action.name()) {
                        case "ADDED":
                            logger.log(Level.INFO, pod.getMetadata().getName() + "got added");
                            break;
                        case "DELETED":
                            logger.log(Level.INFO, pod.getMetadata().getName() + "got deleted");
                            break;
                        case "MODIFIED":
                            logger.log(Level.INFO, pod.getMetadata().getName() + "got modified");
                            break;
                        default:
                            logger.log(Level.SEVERE, "Unrecognized event: " + action.name());
                    }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                    logger.log(Level.INFO, "Closed");
                    isWatchClosed.countDown();
                }
            });

            // Wait till watch gets closed
            isWatchClosed.await();
        } catch (InterruptedException interruptedException) {
            logger.log(Level.INFO, "Thread Interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}
