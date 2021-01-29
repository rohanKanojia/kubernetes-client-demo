package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BookWatch {
    private static MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = null;

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            // Create Book Client
            bookClient = client.customResources(Book.class);

            // Update Book with name effective-java
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            bookClient.inNamespace("default").watch(new Watcher<Book>() {
                @Override
                public void eventReceived(Action action, Book book) {
                    System.out.println(action.name() + " " + book.getMetadata().getName());
                }

                @Override
                public void onClose(WatcherException e) {
                    countDownLatch.countDown();
                    System.out.println("Closing watch");
                }
            });

            System.out.println("Watch opened for 10 minutes");
            countDownLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            interruptedException.printStackTrace();
        }
    }
}
