package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;

public class PodWatchTest {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            Watch watch = client.pods().inNamespace("default").watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {
                   System.out.printf("%s : %s%n", action.name(), pod.getMetadata().getName());
                }

                @Override
                public void onClose(KubernetesClientException e) { }
            });

            // Watch till 10 seconds
            Thread.sleep(10 * 1000);

            // Close Watch
            watch.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
