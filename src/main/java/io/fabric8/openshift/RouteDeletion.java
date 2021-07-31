package io.fabric8.openshift;

import java.util.List;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class RouteDeletion {
	public static void main(String[] args) {
		try (OpenShiftClient client = new DefaultOpenShiftClient()) {
			List<HasMetadata> k8sItems = client.load(RouteDeletion.class.getResourceAsStream("/multiple-routes.yml"))
					.get();

			k8sItems.parallelStream().map(metadata -> client.resource(metadata).delete()).reduce(true,
					Boolean::logicalAnd);
		}
	}
}
