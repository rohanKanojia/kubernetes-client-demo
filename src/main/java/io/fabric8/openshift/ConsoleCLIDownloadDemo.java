package io.fabric8.openshift;

import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ConsoleCLIDownloadDemo {
    public static void main(String[] args) {
        try (OpenShiftClient client = new DefaultOpenShiftClient()) {
            client.console().consoleCLIDownloads().list();
        }
    }
}