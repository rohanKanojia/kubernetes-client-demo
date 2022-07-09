package io.fabric8.demos;

import io.fabric8.crd.Book;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class BookDelete {

    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            // Create Book Client
            MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> bookClient = client.resources(Book.class);

            // Delete a CustomResource
            bookClient.inNamespace("default").withName("kubernetes-patterns").delete();
        }
    }
}
