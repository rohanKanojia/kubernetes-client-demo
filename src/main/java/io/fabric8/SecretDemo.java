package io.fabric8;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.internal.SerializationUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;

public class SecretDemo {
    public static void main(String args[]) throws Exception {
        String dockerConfigStr = "{\"auths\":{\"%s\":{\"username\":\"%s\",\"password\":\"%s\",\"auth\":\"dGVzdDpsb2w=\"}}}";

        // Creating a simple Pull Secret using client
        Secret secret = new SecretBuilder()
                .withNewMetadata().withName("test").endMetadata()
                .withType("kubernetes.io/dockerconfigjson")
                .withData(
                        Collections.singletonMap(".dockerconfigjson",
                                Base64.getEncoder().encodeToString(String.format(dockerConfigStr, null, "test", "lol").getBytes())))
                .build();

        Files.write(Paths.get("/home/rohaan/docker-secret.yml"), SerializationUtils.dumpAsYaml(secret).getBytes());
    }
}
