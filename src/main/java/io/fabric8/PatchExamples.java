package io.fabric8;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;
import java.util.Collections;
import java.util.List;

public class PatchExamples {
    public static void main(String[] args) {
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            Deployment deployment = createNewDeployment("patch-demo", 2);

            deployment = kubernetesClient.apps().deployments()
                    .inNamespace("default")
                    .createOrReplace(deployment);

            standardPatchTyped(kubernetesClient, deployment);
            patchStrategicMerge(kubernetesClient, deployment);
            patchStrategicMergeExplicit(kubernetesClient, deployment);
            patchJson(kubernetesClient, deployment);
            patchJsonMerge(kubernetesClient, deployment);
        }
    }

    private static void patchStrategicMergeExplicit(KubernetesClient kubernetesClient, Deployment deployment) {
        PatchContext patchContext = new PatchContext.Builder()
                .withPatchType(PatchType.STRATEGIC_MERGE)
                .build();

        kubernetesClient.apps().deployments()
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .patch(patchContext, "{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"patch-demo-ctr-2\",\"image\":\"redis\"}]}}}}");
    }

    private static void standardPatchTyped(KubernetesClient kubernetesClient, Deployment deployment) {
        // Prepare modified patch object
        List<Container> containerList = deployment.getSpec().getTemplate().getSpec().getContainers();
        containerList.add(new ContainerBuilder()
                .withName("patch-demo-ctr-2")
                .withImage("redis")
                .build());
        deployment.getSpec().getTemplate().getSpec().setContainers(containerList);

        // Patch to API Server
        kubernetesClient.apps().deployments()
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .patch(deployment);
    }

    private static void patchJsonMerge(KubernetesClient kubernetesClient, Deployment deployment) {
        PatchContext patchContext = new PatchContext.Builder()
                .withPatchType(PatchType.JSON_MERGE)
                .build();

        kubernetesClient.apps().deployments()
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .patch(patchContext, "{\"metadata\":{\"annotations\":{\"foo\":null}}}");
    }

    private static void patchJson(KubernetesClient kubernetesClient, Deployment deployment) {
        PatchContext patchContext = new PatchContext.Builder()
                .withPatchType(PatchType.JSON)
                .build();

        kubernetesClient.apps().deployments()
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .patch(patchContext, "[{\"op\": \"replace\", \"path\":\"/spec/template/spec/containers/0/image\", \"value\":\"nginx:mainline\"}]");
    }

    private static void patchStrategicMerge(KubernetesClient kubernetesClient, Deployment deployment) {
        kubernetesClient.apps().deployments()
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .patch("{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"patch-demo-ctr-2\",\"image\":\"redis\"}]}}}}");
    }

    private static Deployment createNewDeployment(String name, int replicas) {
        return new DeploymentBuilder()
                .withNewMetadata()
                .withName(name)
                .addToAnnotations("foo", "bar")
                .addToAnnotations("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .withReplicas(replicas)
                .withNewSelector()
                .addToMatchLabels(Collections.singletonMap("app", "nginx"))
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("patch-demo-ctr")
                .withImage("nginx")
                .endContainer()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();
    }
}
