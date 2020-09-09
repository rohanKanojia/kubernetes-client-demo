package io.fabric8.openshift;

import io.fabric8.openshift.api.model.EgressNetworkPolicy;
import io.fabric8.openshift.api.model.EgressNetworkPolicyBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class EgressNetworkPolicyTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            EgressNetworkPolicy enp = new EgressNetworkPolicyBuilder()
                    .withNewMetadata()
                    .withName("foo")
                    .withNamespace("default")
                    .endMetadata()
                    .withNewSpec()
                    .addNewEgress()
                    .withType("Allow")
                    .withNewTo()
                    .withCidrSelector("1.2.3.0/24")
                    .endTo()
                    .endEgress()
                    .addNewEgress()
                    .withType("Allow")
                    .withNewTo()
                    .withDnsName("www.foo.com")
                    .endTo()
                    .endEgress()
                    .endSpec()
                    .build();
            client.egressNetworkPolicies().inNamespace("default").createOrReplace(enp);
            System.out.println("Created");
            client.egressNetworkPolicies().inNamespace("default").withName("foo").delete();
            System.out.println("Deleted");
        }
    }
}
