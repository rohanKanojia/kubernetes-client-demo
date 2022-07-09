package io.fabric8;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.DeletionPropagation;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.QuantityBuilder;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class StatefulSetDelete {

    public static void main(String[] argv) throws Exception {

        final String statefulSetName = "test" + System.currentTimeMillis();
        try (final KubernetesClient client = new KubernetesClientBuilder().build()) {
            client.apps().statefulSets().resource(createStatefulSet(statefulSetName)).create();

            StatefulSet statefulSet = client.apps().statefulSets().withName(statefulSetName).waitUntilReady(5, TimeUnit.MINUTES);

            client.resource(statefulSet).withPropagationPolicy(DeletionPropagation.BACKGROUND).delete();

            final boolean exists = client.apps().statefulSets().withName(statefulSetName).get() != null;
            if (exists) {
                throw new IllegalStateException("Statefulset ought to have gone");
            }
            else {
                System.out.println("Statefulset deleted successfully");
            }
        }
    }

    private static StatefulSet createStatefulSet(final String statefulSetName) {
        final PodTemplateSpec podTemplateSpec = new PodTemplateSpecBuilder().withNewMetadata()
                .addToLabels(Collections.singletonMap("foo",
                        "bar"))
                .endMetadata()
                .withNewSpec()
                .withContainers(new ContainerBuilder()
                        .withName(
                                "foocontainer")
                        .withImage("busybox")
                        .withCommand(
                                "/bin/sleep")
                        .withArgs("3600")
                        .build())
                .endSpec()
                .build();

        final ResourceRequirements build = new ResourceRequirementsBuilder().withRequests(Collections.singletonMap(
                "storage",
                new QuantityBuilder().withAmount("1Mi").build())).build();

        return new StatefulSetBuilder()
                .withNewMetadata().withName(statefulSetName)
                .endMetadata()
                .withSpec(new StatefulSetSpecBuilder()
                        .withTemplate(podTemplateSpec)
                        .withVolumeClaimTemplates(new PersistentVolumeClaimBuilder()
                                .withNewMetadata()
                                .withName(statefulSetName + "pvc")
                                .endMetadata()
                                .withNewSpec()
                                .withAccessModes("ReadWriteOnce")
                                .withResources(
                                        build)
                                .endSpec()
                                .build())
                        .withSelector(new LabelSelectorBuilder().withMatchLabels(Collections.singletonMap(
                                "foo",
                                "bar")).build())
                        .build())
                .build();
    }
}
