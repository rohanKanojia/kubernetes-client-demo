package io.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.openshift.api.model.Template;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateDemo {
	private static final Logger logger = Logger.getLogger(TemplateDemo.class
			.getName());

	public static void main(String args[]) {
		try {
			OpenShiftClient openshiftClient = new DefaultOpenShiftClient();
			String namespace = openshiftClient.getNamespace();
			String templateFilePath = System.getProperty("user.dir")
					+ "/src/main/resources/wordpress-template.yml";

			logger.log(Level.INFO, "Creating template.....");
			Template template = openshiftClient.templates()
					.inNamespace(namespace)
					.load(new FileInputStream(templateFilePath)).get();
			openshiftClient.templates().inNamespace(namespace).create(template);
			logger.log(Level.INFO, "Created template");

			openshiftClient.templates().inNamespace(namespace)
					.withName(template.getMetadata().getName()).process();
			logger.log(Level.INFO, "processed template");
			
			final CountDownLatch latch = new CountDownLatch(1);
			Watch watch = openshiftClient.pods().inNamespace(namespace).watch(new Watcher<Pod>() {
				public void eventReceived(
						io.fabric8.kubernetes.client.Watcher.Action action,
						Pod arg1) {
					logger.log(Level.INFO, "pod : " + arg1.getMetadata().getName());
					if(action.equals(Action.MODIFIED)) {
						latch.countDown();
					}
					
				}

				public void onClose(KubernetesClientException arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			latch.await(10, TimeUnit.MINUTES);
			logger.log(Level.INFO, "Closing client now... ");
			openshiftClient.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}
