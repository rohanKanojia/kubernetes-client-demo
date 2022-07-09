package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.cache.Lister;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InformerLister {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            String namespace = "default";
            System.out.println(new Timestamp(System.currentTimeMillis()) + "ConfigMap informer started for namespace: " + namespace);
            SharedIndexInformer<ConfigMap> informer =client.configMaps().inNamespace(namespace).inform(new ResourceEventHandler<ConfigMap>() {
                @Override
                public void onAdd(ConfigMap configMap) {
                    System.out.println(new Timestamp(System.currentTimeMillis()) + " ADD " + configMap.getMetadata().getName());
                }

                @Override
                public void onUpdate(ConfigMap configMap, ConfigMap t1) {
                    System.out.println(new Timestamp(System.currentTimeMillis()) + " UPDATE " + configMap.getMetadata().getName());
                }

                @Override
                public void onDelete(ConfigMap configMap, boolean b) {
                    System.out.println(new Timestamp(System.currentTimeMillis()) + "DELETE " + configMap.getMetadata().getName());
                }
            });
            while(!informer.hasSynced()) {
                Thread.sleep(500L);
                System.out.println(new Timestamp(System.currentTimeMillis()) + " Not Synced");
            }
            Lister<ConfigMap> list = new Lister<>(informer.getIndexer(), namespace);
            List<ConfigMap> configMapList = list.list();
            System.out.println(new Timestamp(System.currentTimeMillis()) + "Lister.list() returns " + configMapList.size() + " items");
            configMapList.stream().map(ConfigMap::getMetadata).map(ObjectMeta::getName).forEach(System.out::println);

            TimeUnit.MINUTES.sleep(40);
            informer.stop();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            interruptedException.printStackTrace();
        }
    }
}
