package io.fabric8;

import io.fabric8.crd.DoneableDummy;
import io.fabric8.crd.Dummy;
import io.fabric8.crd.DummyList;
import io.fabric8.crd.DummySpec;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

public class CustomResourceCreateDemo {
    public static void main(String[] args) {

        try (KubernetesClient client = new DefaultKubernetesClient()) {
            // Create Dummy object
            Dummy dummy = getDummy();

            // Dummy Client
            MixedOperation<Dummy, DummyList, DoneableDummy, Resource<Dummy, DoneableDummy>> dummyClient = null;
            CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                    .Builder()
                    .withGroup("demo.fabric8.io")
                    .withKind("Dummy")
                    .withName("dummies.demo.fabric8.io")
                    .withPlural("dummies")
                    .withScope("Namespaced")
                    .withVersion("v1")
                    .build();

            // Initializing Dummy Client
            dummyClient = client.customResources(context, Dummy.class, DummyList.class, DoneableDummy.class);
            // Using Dummy Client to create Dummy resource
            dummyClient.inNamespace("default").createOrReplace(dummy);
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
