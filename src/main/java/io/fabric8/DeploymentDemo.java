package io.fabric8;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

/*
 * Deploys a basic MySQL container on Cluster and reads logs from it.
 */
public class DeploymentDemo {
	private static final Logger logger = Logger.getLogger(DeploymentDemo.class.getName());
	
	public static void main(String[] args) {
		try (final KubernetesClient client = new KubernetesClientBuilder().build()) {
			String namespace = "default";
			Deployment deployment1 = new DeploymentBuilder()
					.withNewMetadata()
					.withName("deployment1")
					.addToLabels("app", "database")
					.endMetadata()
					.withNewSpec()
					.withReplicas(1)
					.withNewSelector()
					.addToMatchLabels("app", "database")
					.endSelector()
					.withNewTemplate()
					.withNewMetadata()
					.addToLabels("app", "database")
					.addToLabels("category", "deploymentDemo")
					.endMetadata()
					.withNewSpec()
					.addNewContainer()
					.withName("mysql")
					.withImage("openshift/mysql-55-centos7")
					.addNewPort()
					.withContainerPort(3306)
					.endPort()
					.addNewEnv()
					.withName("MYSQL_ROOT_PASSWORD")
					.withValue("password")
					.endEnv()
					.addNewEnv()
					.withName("MYSQL_DATABASE")
					.withValue("foodb")
					.endEnv()
					.addNewEnv()
					.withName("MYSQL_USER")
					.withValue("luke")
					.endEnv()
					.addNewEnv()
					.withName("MYSQL_PASSWORD")
					.withValue("password")
					.endEnv()
					.endContainer()
					.endSpec()
					.endTemplate()
					.endSpec()
					.build();
			
			logger.log(Level.INFO, "Creating deployment ....");
			client.apps().deployments().inNamespace(namespace).resource(deployment1).create();
			logger.log(Level.INFO, "Deployment created..OK");
			
			// Wait for Deployment to come up
			Thread.sleep(10 * 1000L);
			client.apps().deployments().inNamespace(namespace)
					.withName(deployment1.getMetadata().getName())
					.waitUntilReady(1, TimeUnit.MINUTES);
			
			// Get Deployment pod.
			Pod aDeploymentPod = client.pods().inNamespace(namespace).withLabel("category", "deploymentDemo").list().getItems().get(0);
			
			// Get logs from running pod:
			String podLog = client.pods().inNamespace(namespace).withName(aDeploymentPod.getMetadata().getName()).getLog();
			logger.log(Level.INFO, podLog);
		} catch (KubernetesClientException exception1) {
			logger.log(Level.SEVERE, "An exception related to Kubernetes encountered");
			exception1.printStackTrace();
		} catch (InterruptedException exception2) {
			Thread.currentThread().interrupt();
		}
	}
}
