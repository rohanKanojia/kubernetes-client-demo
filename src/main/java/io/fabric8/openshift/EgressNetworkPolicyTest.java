package io.fabric8.openshift;

import io.fabric8.openshift.api.model.EgressNetworkPolicy;
import io.fabric8.openshift.api.model.EgressNetworkPolicyBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class EgressNetworkPolicyTest {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
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
            client.egressNetworkPolicies().inNamespace("default").resource(enp).create();
            System.out.println("Created");
            client.egressNetworkPolicies().inNamespace("default").withName("foo").delete();
            System.out.println("Deleted");
        }
    }
}
