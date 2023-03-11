package io.fabric8.openshift;

import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRule;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRuleBuilder;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRuleList;
import io.fabric8.openshift.client.OpenShiftClient;

public class PrometheusRuleTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            PrometheusRule prometheusRule = new PrometheusRuleBuilder()
                    .withNewMetadata().withName("foo").endMetadata()
                    .withNewSpec()
                    .addNewGroup()
                    .withName("./example-rules")
                    .addNewRule()
                    .withAlert("ExampleAlert")
                    .withNewExpr().withValue("vector(1)").endExpr()
                    .endRule()
                    .endGroup()
                    .endSpec()
                    .build();

            client.monitoring().prometheusRules().inNamespace("rokumar").resource(prometheusRule).create();
            System.out.println("Created");

            PrometheusRuleList prometheusRuleList = client.monitoring().prometheusRules().inNamespace("rokumar").list();
            System.out.println(prometheusRuleList.getItems().size() + " items found");
        }
    }
}