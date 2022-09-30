package io.fabric8.programming.kubernetes.java.customresource;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookInformerDemo {
  private static final Logger logger = LoggerFactory.getLogger(BookInformerDemo.class.getSimpleName());

  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookOp = client.resources(Book.class);

      SharedIndexInformer<Book> bookSharedIndexInformer = bookOp.inNamespace("default").inform(new ResourceEventHandler<>() {
        @Override
        public void onAdd(Book book) { logger.info("{} ADDED"); }

        @Override
        public void onUpdate(Book book, Book t1) { logger.info("{} UPDATED"); }

        @Override
        public void onDelete(Book book, boolean b) { logger.info("{} DELETED"); }
      });

      Thread.sleep(30 * 1000L);
      logger.info("Book SharedIndexInformer open for 30 seconds");

      bookSharedIndexInformer.close();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
