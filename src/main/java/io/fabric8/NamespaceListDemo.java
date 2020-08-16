package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Creates Kubernetes Client and queries for all namespaces in cluster.
 */
public class NamespaceListDemo {
	private static final Logger logger = Logger.getLogger(NamespaceListDemo.class
			.getName());

	public static void main(String[] args) {
		logger.log(Level.INFO, "Creating Kuberntes client");
		try (KubernetesClient client = new DefaultKubernetesClient()) {
			logger.log(Level.INFO, "Kubernetes client successfully created");

			// List all namespaces
			client.namespaces()
					.list()
					.getItems()
					.forEach(n -> logger.log(Level.INFO, n.getMetadata().getName()));

		} catch (KubernetesClientException aException) {
			logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
			aException.printStackTrace();
		}
	}

}
