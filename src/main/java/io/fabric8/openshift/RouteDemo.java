package io.fabric8.openshift;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.api.model.RouteBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RouteDemo {
    private static final Logger logger = Logger.getLogger(RouteDemo.class
            .getName());

    public static void main(String[] args) {

        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            Route route1 = new RouteBuilder()
                    .withApiVersion("v1")
                    .withNewMetadata().withName("route-unsecured").endMetadata()
                    .withNewSpec()
                    .withHost("www.example.com")
                    .withPath("/test")
                    .withNewTo()
                    .withKind("Service")
                    .withName("service-name")
                    .endTo()
                    .endSpec()
                    .build();

            client.routes().inNamespace("rokumar").resource(route1).create();
        } catch (KubernetesClientException aException) {
            logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
            aException.printStackTrace();
        }
    }
}
