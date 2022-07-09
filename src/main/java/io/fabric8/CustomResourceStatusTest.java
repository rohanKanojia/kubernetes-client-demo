package io.fabric8;

import io.fabric8.crd.CronTab;
import io.fabric8.crd.CronTabList;
import io.fabric8.crd.CronTabStatus;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.logging.Logger;

public class CustomResourceStatusTest {
    private static final Logger logger = Logger.getLogger(CustomResourceStatusTest.class.getName());

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            MixedOperation<CronTab, CronTabList, Resource<CronTab>> cronTabClient = client
                    .resources(CronTab.class, CronTabList.class);

            CronTab cronTab = cronTabClient.inNamespace("default").withName("my-new-cron-object").get();

            log(cronTab.getMetadata().getName());
            CronTabStatus newStatus = new CronTabStatus();
            newStatus.setReplicas(3);
            newStatus.setLabelSelector("foobar");
            cronTab.setStatus(newStatus);

            CronTab updatedCronTab = cronTabClient.inNamespace("default").replaceStatus(cronTab);
            log(updatedCronTab.getStatus().getLabelSelector());
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
