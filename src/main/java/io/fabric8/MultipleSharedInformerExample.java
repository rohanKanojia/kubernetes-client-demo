package io.fabric8;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.cache.Lister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MultipleSharedInformerExample {
    private static final Logger logger = LoggerFactory.getLogger("MultiInformer::");
    private static final String NAMESPACE_PREFIX = "multi-";

    public static void main(String[] args) throws InterruptedException {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            Map<String, SharedIndexInformer<Pod>> namespaceToInformerMap = new HashMap<>();
            logger.info("Informer factory initialized.");
            for (int i = 10; i < 30; i++) {
                SharedIndexInformer<Pod> informer = newPodSharedInformerClient(client, NAMESPACE_PREFIX + i);
                namespaceToInformerMap.put(NAMESPACE_PREFIX + i, informer);
            }
            logger.info("Starting all registered informers");

            for (Map.Entry<String, SharedIndexInformer<Pod>> entry: namespaceToInformerMap.entrySet()) {
                listPodsFromEachInformerCache(entry.getKey(), entry.getValue());
            }
            TimeUnit.MINUTES.sleep(15);
            namespaceToInformerMap.forEach((n, i) -> i.stop());
        }
    }

    private static SharedIndexInformer<Pod> newPodSharedInformerClient(KubernetesClient client,
        String namespace) {
        SharedIndexInformer<Pod> podInformer = client.pods().inNamespace(namespace).inform();
        logger.info("Registered Pod SharedInformer for {} namespace", namespace);
        podInformer.addEventHandler(new PodEventHandler());
        return podInformer;
    }

    private static void listPodsFromEachInformerCache(String namespace, SharedIndexInformer<Pod> podInformer) {
        // Wait till Informer syncs
        while (!podInformer.hasSynced() && !Thread.currentThread().isInterrupted());
        Lister<Pod> podLister = new Lister<>(podInformer.getIndexer(), namespace);
        logger.info("PodLister has {}", podLister.list().size());
        podLister.list().stream().map(Pod::getMetadata).map(ObjectMeta::getName).forEach(p -> logger.info("{} namespace Lister list: {}", namespace, p));
    }

    private static final class PodEventHandler implements ResourceEventHandler<Pod> {
        @Override
        public void onAdd(Pod pod) {
            logger.info("{}/{} pod added", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
        }

        @Override
        public void onUpdate(Pod oldPod, Pod newPod) {
            logger.info("{}/{} pod updated", oldPod.getMetadata().getNamespace(), oldPod.getMetadata().getName());
        }

        @Override
        public void onDelete(Pod pod, boolean deletedFinalStateUnknown) {
            logger.info("{}/{} pod deleted", pod.getMetadata().getNamespace(), pod.getMetadata().getName());
        }
    }
}
