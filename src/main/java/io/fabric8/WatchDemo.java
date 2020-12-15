package io.fabric8;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.api.builder.TypedVisitor;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;

public class WatchDemo {
	private static final Logger logger = Logger.getLogger(WatchDemo.class
			.getName());

	public static void main(String[] args) {
		KubernetesClient client = new DefaultKubernetesClient();
		String currentNamespace = client.getNamespace();
		try {
			Pod pod1 = new PodBuilder().withNewMetadata()
					.withName("sample-watch-pod").endMetadata().withNewSpec()
					.addNewContainer().withName("nginx").withImage("nginx")
					.endContainer().endSpec().build();

			client.pods().inNamespace(currentNamespace).create(pod1);

			final CountDownLatch latch = new CountDownLatch(1);
			Watch watch = client.pods().inNamespace(currentNamespace)
					.withName("sample-watch-pod").watch(new Watcher<Pod>() {
						@Override
						public void eventReceived(Action action, Pod pod) {

							if (action.equals(Action.MODIFIED)) {
								logger.log(Level.INFO,
										"Action : " + action.name()
												+ " Pod name : "
												+ pod.getMetadata().getName());
								latch.countDown();
							}
						}

						@Override
						public void onClose() { }

						@Override
						public void onClose(WatcherException e) { }

					});

			latch.await(1, TimeUnit.MINUTES);
			client.pods().inNamespace(currentNamespace)
					.withName("sample-watch-pod").edit(new TypedVisitor<ObjectMetaBuilder>() {
				@Override
				public void visit(ObjectMetaBuilder o) {
					o.addToLabels("foo", "bar");
				}
			});

			watch.close();
			logger.log(Level.INFO, "Closing client now...");
			client.close();

		} catch (KubernetesClientException exception) {
			exception.printStackTrace();
		} catch(InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	}
}
