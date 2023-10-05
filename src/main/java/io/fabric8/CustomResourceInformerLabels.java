package io.fabric8;

import io.fabric8.crd.mode.v1.Dummy;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;

public class CustomResourceInformerLabels {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      ResourceEventHandler<Dummy> myEventHandler = new ResourceEventHandler<>() {
        @Override
        public void onAdd(Dummy dummy) {
          System.out.printf("ADDED %s/%s\n", dummy.getMetadata().getNamespace(), dummy.getMetadata().getName());
        }

        @Override
        public void onUpdate(Dummy dummy, Dummy t1) {
          System.out.printf("MODIFIED %s/%s\n", dummy.getMetadata().getNamespace(), dummy.getMetadata().getName());

        }

        @Override
        public void onDelete(Dummy dummy, boolean b) {
          System.out.printf("DELETED %s/%s\n", dummy.getMetadata().getNamespace(), dummy.getMetadata().getName());
        }
      };
      SharedIndexInformer<Dummy> informer = client.resources(Dummy.class)
          .inNamespace("default")
          .withLabel("app", "test-operator")
          .inform(myEventHandler, 30 * 1000L);

      informer.close();
    }
  }
}
