package io.fabric8;

import io.fabric8.crd.CronTab;
import io.fabric8.crd.CronTabList;
import io.fabric8.crd.CronTabStatus;
import io.fabric8.crd.DoneableCronTab;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.IOException;
import java.util.logging.Logger;

public class CustomResourceStatusTest {
    private static final Logger logger = Logger.getLogger(CustomResourceStatusTest.class.getName());

    public static void main(String args[]) throws IOException {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            CustomResourceDefinition cronTabCrd = client.customResourceDefinitions().load(CustomResourceStatusTest.class.getResourceAsStream("/crontab-crd.yml")).get();

            MixedOperation<CronTab, CronTabList, DoneableCronTab, Resource<CronTab, DoneableCronTab>> cronTabClient = client
                    .customResources(cronTabCrd, CronTab.class, CronTabList.class, DoneableCronTab.class);

            CronTab cronTab = cronTabClient.inNamespace("default").withName("my-new-cron-object").get();

            log(cronTab.getMetadata().getName());
            CronTabStatus newStatus = new CronTabStatus();
            newStatus.setReplicas(3);
            newStatus.setLabelSelector("foobar");
            cronTab.setStatus(newStatus);

            CustomResourceDefinitionContext crdContext = new CustomResourceDefinitionContext.Builder()
                    .withGroup("stable.example.com")
                    .withPlural("crontabs")
                    .withVersion("v1")
                    .withScope("Namespaced")
                    .build();


            client.customResource(crdContext).updateStatus("default", "my-new-cron-object", CustomResourceStatusTest.class.getResourceAsStream("/crontab-cr.json"));

            CronTab updatedCronTab = cronTabClient.inNamespace("default").updateStatus(cronTab);
            log(updatedCronTab.getStatus().getLabelSelector());
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
