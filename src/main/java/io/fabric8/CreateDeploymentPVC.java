package io.fabric8;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Collections;

public class CreateDeploymentPVC {
    public static void main(String[] args) {
        try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().build()) {
            PersistentVolumeClaim pvc = createNewPvc();
            Deployment deployment = createNewDeployment();

            kubernetesClient.persistentVolumeClaims().inNamespace("default").resource(pvc).createOrReplace();
            kubernetesClient.apps().deployments().inNamespace("default").resource(deployment).createOrReplace();
        }
    }

    private static Deployment createNewDeployment() {
        return new DeploymentBuilder()
                .withNewMetadata().withName("test-deploy").endMetadata()
                .withNewSpec()
                .withNewSelector()
                .withMatchLabels(Collections.singletonMap("app", "test"))
                .endSelector()
                .withReplicas(1)
                .withNewTemplate()
                .withNewMetadata()
                .withLabels(Collections.singletonMap("app", "test"))
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withImage("httpd:2.4-alpine")
                .withName("httpd")
                .addNewVolumeMount()
                .withMountPath("/data")
                .withName("test-data")
                .endVolumeMount()
                .endContainer()
                .addNewVolume()
                .withName("test-data")
                .withNewPersistentVolumeClaim().withClaimName("test-vol").endPersistentVolumeClaim()
                .endVolume()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();
    }

    private static PersistentVolumeClaim createNewPvc() {
        return new PersistentVolumeClaimBuilder()
                .withNewMetadata().withName("test-vol").endMetadata()
                .withNewSpec()
                .withAccessModes("ReadWriteOnce")
                .withNewResources()
                .withRequests(Collections.singletonMap("storage", new Quantity("1Gi")))
                .endResources()
                .endSpec()
                .build();
    }
}