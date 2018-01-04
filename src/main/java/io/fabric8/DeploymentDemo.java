package io.fabric8;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

/*
 * Deploys a basic MySQL container on Cluster and reads logs from it.
 */
public class DeploymentDemo {
	private static final Logger logger = Logger.getLogger(DeploymentDemo.class.getName());
	
	public static void main(String args[]) {
		try {
			String namespace = "default";
			final KubernetesClient client = new DefaultKubernetesClient();
			Deployment deployment1 = new DeploymentBuilder().withNewMetadata()
					.withName("deployment1").endMetadata().withNewSpec()
					.withReplicas(1).withNewTemplate().withNewMetadata()
					.addToLabels("app", "database")
					.addToLabels("category", "deploymentDemo").endMetadata().withNewSpec()
					.addNewContainer().withName("mysql")
					.withImage("openshift/mysql-55-centos7").addNewPort()
					.withContainerPort(3306).endPort().addNewEnv()
					.withName("MYSQL_ROOT_PASSWORD").withValue("password")
					.endEnv().addNewEnv().withName("MYSQL_DATABASE")
					.withValue("foodb").endEnv().addNewEnv()
					.withName("MYSQL_USER").withValue("luke").endEnv()
					.addNewEnv().withName("MYSQL_PASSWORD")
					.withValue("password").endEnv().endContainer().endSpec()
					.endTemplate().endSpec().build();
			
			logger.log(Level.INFO, "Creating deployment ....");
			client.extensions().deployments().inNamespace(namespace).create(deployment1);
			logger.log(Level.INFO, "Deployment created..OK");
			
			// Wait for Deployment to come up
			Thread.sleep(10 * 1000);
			
			// Get Deployment pod.
			Pod aDeploymentPod = client.pods().inNamespace(namespace).withLabel("category", "deploymentDemo").list().getItems().get(0);
			
			// Get logs from running pod:
			String podLog = client.pods().inNamespace(namespace).withName(aDeploymentPod.getMetadata().getName()).getLog();
			logger.log(Level.INFO, "Pod Logs: " + podLog);
			
			client.close();
		} catch (KubernetesClientException exception1) {
			logger.log(Level.SEVERE, "An exception related to Kubernetes encountered");
			exception1.printStackTrace();
		} catch (InterruptedException exception2) {
			
		}
	}
}
