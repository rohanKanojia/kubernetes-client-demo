package io.fabric8.openshift;

import io.fabric8.openshift.api.model.operatorhub.v1alpha1.CatalogSource;
import io.fabric8.openshift.api.model.operatorhub.v1alpha1.CatalogSourceBuilder;
import io.fabric8.openshift.api.model.operatorhub.v1alpha1.CatalogSourceList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class CatalogSourceTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            CatalogSource cs = new CatalogSourceBuilder()
                    .withNewMetadata().withName("foo").endMetadata()
                    .withNewSpec()
                    .withSourceType("Foo")
                    .withImage("nginx:latest")
                    .withDisplayName("Foo Bar")
                    .withPublisher("Fabric8")
                    .endSpec()
                    .build();
            client.operatorHub().catalogSources().inNamespace("default").createOrReplace(cs);
            System.out.println("Created.");

            CatalogSourceList csList = client.operatorHub().catalogSources().inNamespace("default").withLabel("foo", "bar").list();
            System.out.println(csList.getItems().size() + " items found.");

            client.operatorHub().catalogSources().inNamespace("default").withName("foo").delete();
        }
    }
}