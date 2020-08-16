package io.fabric8;

import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class JobCreate {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
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

            client.batch().jobs().inNamespace("default").createOrReplace(job);
        }
    }
}