package io.fabric8;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.builder.TypedVisitor;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class RollingUpdateDemo {
	private static final Logger logger = Logger
			.getLogger(RollingUpdateDemo.class.getName());

	public static void main(String[] args) {
		String namespace = "default";
		String redisYamlFile = "/postgres-deployment.yml";
		try (KubernetesClient client = new KubernetesClientBuilder().build()) {
			Deployment aDeployment = client.apps().deployments()
					.inNamespace(namespace)
					.load(RollingUpdateDemo.class.getResourceAsStream(redisYamlFile)).get();

			client.apps().deployments().inNamespace(namespace)
					.resource(aDeployment)
					.create();

			logger.log(Level.INFO, "Successfully created deployment");
			Thread.sleep(20 * 1000);

			// Let's change the image of postgres container
			logger.log(Level.INFO, "updating postgres image ....");
			client.apps().deployments().inNamespace(namespace)
					.withName(aDeployment.getMetadata().getName())
					.edit(new TypedVisitor<PodTemplateSpecBuilder>() {
						@Override
						public void visit(PodTemplateSpecBuilder o) {
							o.editSpec().editContainer(0)
									.withImage("centos/postgresql-94-centos7").endContainer()
									.endSpec();
						}
					});
			logger.log(Level.INFO, "Updated container image... OK");
			
			
			logger.log(Level.INFO, "Closing client now...");
		} catch (Exception exception1) {
			exception1.printStackTrace();
		}
	}
}
