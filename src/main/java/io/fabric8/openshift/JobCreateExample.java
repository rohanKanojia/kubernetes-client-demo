package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class JobCreateExample {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            Job job = new JobBuilder()
                    .withNewMetadata().withName("pi").endMetadata()
                    .withNewSpec()
                    .withNewTemplate()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("pi")
                    .withImage("perl")
                    .withCommand("perl", "-Mbignum=bpi", "-wle", "print bpi(2000)")
                    .endContainer()
                    .withRestartPolicy("Never")
                    .endSpec()
                    .endTemplate()
                    .withBackoffLimit(4)
                    .endSpec()
                    .build();

            client.batch().v1().jobs().inNamespace("default").resource(job).createOrReplace();
        }
    }
}
