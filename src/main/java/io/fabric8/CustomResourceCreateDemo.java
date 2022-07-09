package io.fabric8;

import io.fabric8.crd.Dummy;
import io.fabric8.crd.DummyList;
import io.fabric8.crd.DummySpec;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class CustomResourceCreateDemo {
    public static void main(String[] args) {

        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            // Create Dummy object
            Dummy dummy = getDummy();

            // Dummy Client
            MixedOperation<Dummy, DummyList, Resource<Dummy>> dummyClient = client.resources(Dummy.class, DummyList.class);
            // Using Dummy Client to create Dummy resource
            dummyClient.inNamespace("default").resource(dummy).createOrReplace();
        }

    }

    private static Dummy getDummy() {
        Dummy dummy = new Dummy();
        dummy.setMetadata(new ObjectMetaBuilder().withName("dummy1").build());
        DummySpec dummySpec = new DummySpec();
        dummySpec.setBar("fur");
        dummySpec.setFoo("goo");
        dummy.setSpec(dummySpec);

        return dummy;
    }
}
