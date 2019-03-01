package io.fabric8;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

public class RollingUpdateDemo {
	private static final Logger logger = Logger
			.getLogger(RollingUpdateDemo.class.getName());

	public static void main(String args[]) throws JsonProcessingException {
		String namespace = "default";
		String redisYamlFile = System.getProperty("user.dir")
				+ "/src/main/resources/postgres-deployment.yml";
		try {
			KubernetesClient client = new DefaultKubernetesClient();

			Deployment aDeployment = client.extensions().deployments()
					.inNamespace(namespace)
					.load(new FileInputStream(redisYamlFile)).get();

			client.extensions().deployments().inNamespace(namespace)
					.create(aDeployment);

			logger.log(Level.INFO, "Successfully created deployment");
			Thread.sleep(20 * 1000);

			// Let's change the image of postgres container
			logger.log(Level.INFO, "updating postgres image ....");
			client.extensions().deployments().inNamespace(namespace)
					.withName(aDeployment.getMetadata().getName()).edit()
					.editSpec().editTemplate().editSpec().editContainer(0)
					.withImage("centos/postgresql-94-centos7").endContainer()
					.endSpec().endTemplate().endSpec().done();
			logger.log(Level.INFO, "Updated container image... OK");
			
			
			logger.log(Level.INFO, "Closing client now...");
			client.close();
		} catch (KubernetesClientException exception1) {
			exception1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}
}
