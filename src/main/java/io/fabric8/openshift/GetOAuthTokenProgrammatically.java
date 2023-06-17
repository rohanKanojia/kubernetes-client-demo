package io.fabric8.openshift;

import io.fabric8.kubernetes.client.utils.Serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GetOAuthTokenProgrammatically {
    public static void main(String[] args) throws FileNotFoundException {
        String user = "rohanKanojia/api-rh-idev-openshift-com:443";
        System.out.printf("Token for %s : %s", user, getOAuthTokenForUser(user));
    }

    public static String getOAuthTokenForUser(String userName) throws FileNotFoundException {
        // Gets KubeConfig via reading KUBECONFIG environment variable or ~/.kube/config
        File kubeConfigFile = new File(io.fabric8.kubernetes.client.Config.getKubeconfigFilename());

        io.fabric8.kubernetes.api.model.Config config = Serialization.unmarshal(new FileInputStream(kubeConfigFile), io.fabric8.kubernetes.api.model.Config.class);

        return config.getUsers()
                .stream()
                .filter(u -> u.getName().equals(userName))
                .findFirst()
                .map(namedAuthInfo -> namedAuthInfo.getUser().getToken()).orElse(null);
    }
}
