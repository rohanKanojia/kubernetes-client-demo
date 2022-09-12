package io.fabric8;

import io.fabric8.crd.CronTab;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class TypedCustomResourceDemoBlog {
    public static void main(String[] args) throws InterruptedException {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            MixedOperation<CronTab, KubernetesResourceList<CronTab>, Resource<CronTab>> cronTabClient = client.resources(CronTab.class);

            // Load CronTab CustomResource from file
            CronTab cronTab1 = cronTabClient.load(TypedCustomResourceDemoBlog.class.getResourceAsStream("/crontab.yml")).get();
            CronTab cronTab2 = cronTabClient.load(TypedCustomResourceDemoBlog.class.getResourceAsStream("/crontab2.yml")).get();

            // Create CronTab
            cronTabClient.inNamespace("default").resource(cronTab1).create();
            cronTabClient.inNamespace("default").resource(cronTab2).create();

            // Get resource from API Server
            cronTab1 = cronTabClient.inNamespace("default").withName("my-new-cron-object").get();
            log(cronTab1.getMetadata().getName() + " created.");

            // List resource from API Server
            KubernetesResourceList<CronTab> cronTabList = cronTabClient.inNamespace("default").list();
            log(cronTabList.getItems().size() + " CronTabs found in cluster");

            // Update resource
            cronTab1.getSpec().setReplicas(5);
            cronTabClient.inNamespace("default").resource(cronTab1).patch();
            log("CronTab " + cronTab1.getMetadata().getName() + " updated.");

            // Delete CronTab
            cronTabClient.inNamespace("default").resource(cronTab1).delete();
            cronTabClient.inNamespace("default").withName("my-second-cron-object").delete();
            log("CronTabs successfully deleted");


            cronTabClient.inNamespace("default").watch(new Watcher<>() {
                @Override
                public void eventReceived(Action action, CronTab cronTab) {
                    log(action.toString() + " " + cronTab.getMetadata().getName());
                }

                @Override
                public void onClose() {
                }

                @Override
                public void onClose(WatcherException e) {
                    log("watch closed...");
                }

            });

            log("Watch open for 60 seconds...");
            Thread.sleep(60 * 1000L);
        }
    }

    private static void log(String action) {
        System.out.println(action);
    }
}
