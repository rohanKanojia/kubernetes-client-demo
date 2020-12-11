package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
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
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            client.pods().inNamespace(namespace).watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {
                    logger.info( action.name() + " " + pod.getMetadata().getName());
                    switch (action.name()) {
                        case "ADDED":
                            logger.info( pod.getMetadata().getName() + "got added");
                            break;
                        case "DELETED":
                            logger.info( pod.getMetadata().getName() + "got deleted");
                            break;
                        case "MODIFIED":
                            logger.info( pod.getMetadata().getName() + "got modified");
                            break;
                        default:
                            logger.error("Unrecognized event: " + action.name());
                    }
                }

                @Override
                public void onClose() { }

                @Override
                public void onClose(WatcherException e) {
                    logger.info( "Closed");
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
