package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionLeaks {
    public static Logger logger = Logger.getLogger(ConnectionLeaks.class.getName());

    public static void main(String[] args) throws InterruptedException {
        KubernetesClient client = new KubernetesClientBuilder().build();
        testConnectionLeaksAfterExecutingCommandInNonExistingPodOnRealServer(client);
    }

    public static void testConnectionLeaksAfterExecutingCommandInNonExistingPodOnRealServer(KubernetesClient client)
            throws InterruptedException {
        logger.info("testConnectionLeaksAfterExecutingCommandInNonExistingPodOnRealServer");
        testConnectionLeaks(client);
    }

    private static void testConnectionLeaks(KubernetesClient client) throws InterruptedException {

        int i = 1;
        while (true) {
            logger.info("#######################################################");
            logger.info("####                                               ####");
            logger.info(
                    "####                   " + String.format("Iteration #%3d", i++) + "              ####");
            logger.info("####                                               ####");
            logger.info("#######################################################");
            doIteration(client);
            System.gc();
        }
    }

    private static void doIteration(KubernetesClient client) throws InterruptedException {
        logger.info("Try to execute 5 echo command in non-existing pod");
        doExec(client);
        doExec(client);
        doExec(client);
        doExec(client);
        doExec(client);

        logger.info("Wait 1 minute");
        Thread.sleep(60 * 1000); // 1 minutes
    }

    private static void doExec(KubernetesClient client) {
        try (ExecWatch exec =
                     client
                             .pods()
                             .inNamespace("test")
                             .withName("non-existing")
                             .redirectingError()
                             .exec("sh", "-c", "echo hello")) {
            Thread.sleep(1000);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "ERROR: Exception occurred: " + e.getMessage());
        }
    }

}
