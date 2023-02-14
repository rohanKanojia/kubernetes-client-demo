package io.fabric8;

import io.fabric8.kubernetes.api.model.batch.v1.CronJob;
import io.fabric8.kubernetes.api.model.batch.v1.CronJobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ManualCronJobTrigger {
    public static void main(String[] args) {
        try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().build()) {
            createNewCronJob(kubernetesClient);
            createManualJob(kubernetesClient, "default", "hello", "hello-rokumar");
        }
    }

    private static Job createManualJob(KubernetesClient kubernetesClient, String namespace, String cronJobName, String jobName) {
        CronJob cronJob = kubernetesClient.batch().v1().cronjobs().inNamespace(namespace).withName(cronJobName).get();

        Job newJobToCreate = new JobBuilder()
                .withNewMetadata()
                .withName(jobName)
                .addNewOwnerReference()
                .withApiVersion("batch/v1beta1")
                .withKind("CronJob")
                .withName(cronJob.getMetadata().getName())
                .withUid(cronJob.getMetadata().getUid())
                .endOwnerReference()
                .addToAnnotations("cronjob.kubernetes.io/instantiate", "manual")
                .endMetadata()
                .withSpec(cronJob.getSpec().getJobTemplate().getSpec())
                .build();

        return kubernetesClient.batch().v1().jobs().inNamespace(namespace).resource(newJobToCreate).create();
    }

    private static CronJob createNewCronJob(KubernetesClient kubernetesClient) {
        CronJob cronJob = new CronJobBuilder()
            .withNewMetadata()
            .withName("hello")
            .endMetadata()
            .withNewSpec()
            .withSchedule("* * * * *")
            .withNewJobTemplate()
            .withNewSpec()
            .withNewTemplate()
            .withNewSpec()
            .addNewContainer()
            .withName("hello")
            .withImage("busybox:1.28")
            .withImagePullPolicy("IfNotPresent")
            .withCommand("/bin/sh", "-c", "date; echo Hello from the Kubernetes cluster")
            .endContainer()
            .withRestartPolicy("OnFailure")
            .endSpec()
            .endTemplate()
            .endSpec()
            .endJobTemplate()
            .endSpec()
            .build();
        return kubernetesClient.batch().v1().cronjobs().inNamespace("default").resource(cronJob).create();
    }
}
