package io.fabric8.openshift;


import io.fabric8.openshift.api.model.ImageStream;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

public class ImageStreamCreateOrReplace {
    public static void main(String[] args) {
        try (OpenShiftClient client = new KubernetesClientBuilder().build().adapt(OpenShiftClient.class)) {
            ImageStream imageStream = client.imageStreams()
                    .load(ImageStreamCreateOrReplace.class.getResourceAsStream("/test-imagestream.yml"))
                    .get();

            client.imageStreams().inNamespace("myproject").resource(imageStream).createOrReplace();

            imageStream.getSpec().setDockerImageRepository("docker.io/openshift/ruby-20");

            client.imageStreams().inNamespace("myproject").resource(imageStream).createOrReplace();
        }
    }
}