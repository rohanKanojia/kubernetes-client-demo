package io.fabric8;

import io.fabric8.crd.mode.v1.CronTab;
import io.fabric8.crd.mode.v1.CronTabStatus;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.logging.Logger;

public class CustomResourceStatusTest {
    private static final Logger logger = Logger.getLogger(CustomResourceStatusTest.class.getName());

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            MixedOperation<CronTab, KubernetesResourceList<CronTab>, Resource<CronTab>> cronTabClient = client
                    .resources(CronTab.class);

            CronTab cronTab = cronTabClient.inNamespace("default").withName("my-new-cron-object").get();

            log(cronTab.getMetadata().getName());
            CronTabStatus newStatus = new CronTabStatus();
            newStatus.setReplicas(3);
            newStatus.setLabelSelector("foobar");
            cronTab.setStatus(newStatus);

            CronTab updatedCronTab = cronTabClient.inNamespace("default").resource(cronTab).updateStatus();
            log(updatedCronTab.getStatus().getLabelSelector());
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
