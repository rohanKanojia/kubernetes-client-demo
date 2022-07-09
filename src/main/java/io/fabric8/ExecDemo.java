package io.fabric8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;

public class ExecDemo {
	private static final Logger logger = Logger.getLogger(ExecDemo.class
			.getName());

  public static void main(String[] args) {
		try (KubernetesClient client = new KubernetesClientBuilder().build()) {
			String namespace = "default";
			Pod pod1 = new PodBuilder().withNewMetadata().withName("pod1")
					.endMetadata().withNewSpec().addNewContainer()
					.withName("mysql").withImage("openshift/mysql-55-centos7")
					.addNewPort().withContainerPort(3306).endPort().addNewEnv()
					.withName("MYSQL_ROOT_PASSWORD").withValue("password")
					.endEnv().addNewEnv().withName("MYSQL_DATABASE")
					.withValue("foodb").endEnv().addNewEnv()
					.withName("MYSQL_USER").withValue("luke").endEnv()
					.addNewEnv().withName("MYSQL_PASSWORD")
					.withValue("password").endEnv().endContainer().endSpec()
					.build();
			
			client.pods().inNamespace(namespace).resource(pod1).createOrReplace();
			logger.log(Level.INFO, "created pod");

			logger.log(Level.INFO, "Waiting for the pod to start");
			client.pods().inNamespace(namespace).withName(pod1.getMetadata().getName()).waitUntilReady(1, TimeUnit.MINUTES);

			logger.log(Level.INFO, "executing a simple command");
			ExecWatch execWatch = client.pods().inNamespace(namespace)
					.withName("pod1").redirectingInput()
					.writingOutput(System.out).writingError(System.err)
					.withTTY().usingListener(new SimpleListener()).exec("ls");

			OutputStream input = execWatch.getInput();
			input.write("hello".getBytes());
			input.flush();

			execWatch.close();

			execWatch.exitCode().join();
			client.pods().inNamespace(namespace).withName("pod1").delete();
			logger.info("Closing client now...");
		} catch (KubernetesClientException | IOException exception) {
			exception.printStackTrace();
		}
	}

	private static class SimpleListener implements ExecListener {

		public void onClose(int arg0, String arg1) {
			// TODO Auto-generated method stub
			logger.log(Level.INFO, "Closing shell now");
		}

		public void onFailure(Throwable arg0, Response arg1) {
			// TODO Auto-generated method stub
			logger.log(Level.SEVERE, "The shell is failed somehow");
		}

		public void onOpen(Response arg0) {
			// TODO Auto-generated method stub
			logger.log(Level.INFO, "The shell is open now...");
		}
	}
}
