package io.fabric8.openshift;


import io.fabric8.openshift.api.model.ImageStream;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ImageStreamCreateOrReplace {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            ImageStream imageStream = client.imageStreams()
                    .load(ImageStreamCreateOrReplace.class.getResourceAsStream("/test-imagestream.yml"))
                    .get();

            client.imageStreams().inNamespace("myproject").createOrReplace(imageStream);

            imageStream.getSpec().setDockerImageRepository("docker.io/openshift/ruby-20");

            client.imageStreams().inNamespace("myproject").createOrReplace(imageStream);
        }
    }
}