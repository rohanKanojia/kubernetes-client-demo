package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeploymentConfigDemo {
	private static final Logger logger = Logger.getLogger(DeploymentConfigDemo.class.getName());
	
	public static void main(String args[]) {
		try {
			final OpenShiftClient client = new DefaultOpenShiftClient();
			String namespace = client.getNamespace();
			DeploymentConfig deploymentConfig1 = new DeploymentConfigBuilder().withNewMetadata()
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
			
			logger.log(Level.INFO, "Creating deployment config....");
			client.deploymentConfigs().inNamespace(namespace).create(deploymentConfig1);
			
			client.close();
		} catch (KubernetesClientException exception1) {
			logger.log(Level.SEVERE, "An exception related to Kubernetes encountered");
			exception1.printStackTrace();
		}
	}
}
