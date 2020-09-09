package io.fabric8.openshift;


import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitor;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitorBuilder;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitorList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ServiceMonitorTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
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

            client.monitoring().serviceMonitors().inNamespace("rokumar").createOrReplace(serviceMonitor);
            System.out.println("created");

            ServiceMonitorList serviceMonitorList = client.monitoring().serviceMonitors().inNamespace("rokumar").list();
            System.out.println(serviceMonitorList.getItems().size() + " items found.");
        }
    }
}