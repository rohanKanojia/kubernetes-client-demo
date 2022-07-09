package io.fabric8.openshift;

import java.util.List;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class RouteDeletion {
	public static void main(String[] args) {
		try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
			List<HasMetadata> k8sItems = client.load(RouteDeletion.class.getResourceAsStream("/multiple-routes.yml"))
					.get();

			k8sItems.parallelStream().forEach(k -> client.resource(k).delete());
		}
	}
}
