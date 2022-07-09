package io.fabric8.openshift;


import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitor;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitorBuilder;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitorList;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class ServiceMonitorTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            ServiceMonitor serviceMonitor = new ServiceMonitorBuilder()
                    .withNewMetadata()
                    .withName("foo")
                    .addToLabels("prometheus", "frontend")
                    .endMetadata()
                    .withNewSpec()
                    .withNewNamespaceSelector().withAny(true).endNamespaceSelector()
                    .withNewSelector()
                    .addToMatchLabels("prometheus", "frontend")
                    .endSelector()
                    .addNewEndpoint()
                    .withPort("http-metric")
                    .withInterval("15s")
                    .endEndpoint()
                    .endSpec()
                    .build();

            client.monitoring().serviceMonitors().inNamespace("rokumar").resource(serviceMonitor).createOrReplace();
            System.out.println("created");

            ServiceMonitorList serviceMonitorList = client.monitoring().serviceMonitors().inNamespace("rokumar").list();
            System.out.println(serviceMonitorList.getItems().size() + " items found.");
        }
    }
}