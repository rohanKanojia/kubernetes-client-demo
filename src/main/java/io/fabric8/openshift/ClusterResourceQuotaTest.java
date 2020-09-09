package io.fabric8.openshift;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceQuotaSpecBuilder;
import io.fabric8.openshift.api.model.ClusterResourceQuota;
import io.fabric8.openshift.api.model.ClusterResourceQuotaBuilder;
import io.fabric8.openshift.api.model.ClusterResourceQuotaList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

import java.util.HashMap;
import java.util.Map;

public class ClusterResourceQuotaTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            Map<String, Quantity> hard = new HashMap<>();
            hard.put("pods", new Quantity("10"));
            hard.put("secrets", new Quantity("20"));
            ClusterResourceQuota acrq = new ClusterResourceQuotaBuilder()
                    .withNewMetadata().withName("foo").endMetadata()
                    .withNewSpec()
                    .withNewSelector()
                    .addToAnnotations("openshift.io/requester", "foo-user")
                    .endSelector()
                    .withQuota(new ResourceQuotaSpecBuilder()
                            .withHard(hard)
                            .build())
                    .endSpec()
                    .build();

            client.quotas().clusterResourceQuotas().createOrReplace(acrq);

            ClusterResourceQuotaList clusterResourceQuotaList = client.quotas().clusterResourceQuotas().list();
            client.quotas().clusterResourceQuotas().withName("foo").delete();
        }
    }
}