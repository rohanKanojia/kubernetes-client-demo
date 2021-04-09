package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.LocalPortForward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PortForwardDemo {
    private static final Logger logger = LoggerFactory.getLogger(PortForwardDemo.class);

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            String podName = "getting-started-7bc7b9d47b-jndfs";

            logger.info("Starting Port Forward for pod: {} {} -> {}", podName, 80, 8080);
            LocalPortForward portForward = client.pods()
                    .inNamespace("default")
                    .withName(podName)
                    .portForward(8080, 8089);

            Thread.sleep(60 * 1000);
            logger.info("Closing...");
            portForward.close();
        } catch (InterruptedException | IOException interruptedException) {
            Thread.currentThread().interrupt();
            interruptedException.printStackTrace();
        }
    }
}
