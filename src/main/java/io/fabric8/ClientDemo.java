package io.fabric8;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Creates Kubernetes Client and queries for all namespaces in cluster.
 */
public class ClientDemo {
	private static final Logger logger = Logger.getLogger(DeploymentDemo.class
			.getName());

	public static void main(String args[]) {

		try {

			logger.log(Level.INFO, "Creating Kuberntes client");
			
			KubernetesClient client = new DefaultKubernetesClient();
			
			logger.log(Level.INFO, "Kubernetes client successfully created");
			
			
			/**
			 * List all the namespaces.
			 */
			NamespaceList aList = client.namespaces().list();
			for(Namespace aNamespace : aList.getItems())
				logger.log(Level.INFO, aNamespace.getMetadata().getName());
			
			client.close();
		} catch (KubernetesClientException aException) {
			logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
			aException.printStackTrace();
		}
	}

}
