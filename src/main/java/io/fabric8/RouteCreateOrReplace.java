package io.fabric8;

import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class RouteCreateOrReplace {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            Route aRoute = client.routes().inNamespace("rokumar").withName("weather-web-application").get();
            aRoute.getMetadata().getAnnotations().put("foo", "bar");
            client.routes().inNamespace("rokumar").createOrReplace(aRoute);
        }
    }
}