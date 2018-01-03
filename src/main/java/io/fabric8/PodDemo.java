package io.fabric8;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

public class PodDemo {
	private static final Logger logger = Logger.getLogger(PodDemo.class
			.getName());

	public static void main(String args[]) throws InterruptedException {
		String namespace = "default";
		String redisYamlFile = System.getProperty("user.dir") + "/src/main/resources/test-redis.yml";

		try {
			final KubernetesClient client = new DefaultKubernetesClient();

			Pod aPod = new PodBuilder().withNewMetadata().withName("demo-pod1")
					.endMetadata().withNewSpec().addNewContainer()
					.withName("nginx").withImage("nginx:1.7.9").addNewPort()
					.withContainerPort(80).endPort().endContainer().endSpec()
					.build();

			/*
			 * Creating a simple pod from Builder.
			 */
			logger.log(Level.INFO, "Creating pod demo-pod1");
			client.pods().inNamespace(namespace).create(aPod);
			logger.log(Level.INFO, "Successfully created pod");

			/*
			 * Creating pod from yaml file
			 */
			logger.log(Level.INFO, redisYamlFile);
			Pod pod2 = client.pods().inNamespace(namespace).load(new FileInputStream(redisYamlFile)).get();
			client.pods().inNamespace(namespace).create(pod2);
			logger.log(Level.INFO, "created " + pod2.getMetadata().getName());

			client.close();
		} catch (KubernetesClientException aException) {
			logger.log(Level.SEVERE,
					"Problem encountered with Kubernetes client!!");
			aException.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Exception encountered : " + e.getMessage());
		}
	}
}
