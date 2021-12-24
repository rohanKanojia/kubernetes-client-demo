package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExecDemoSaveOutput {
    public static void main(String[] args) {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            final CountDownLatch execLatch = new CountDownLatch(1);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ExecWatch execWatch = client.pods().inNamespace("default").withName("spring-boot-camel-1-hl6xf")
                    .writingOutput(out).withTTY().usingListener(new ExecListener() {
                        @Override
                        public void onOpen() {
                            System.out.println("Shell was opened");
                        }

                        @Override
                        public void onFailure(Throwable throwable, Response response) {
                            System.out.println("Shell barfed");
                            execLatch.countDown();
                        }

                        @Override
                        public void onClose(int i, String s) {
                            execLatch.countDown();
                        }
                    }).exec("ls", "/tmp");

            execLatch.await(5, TimeUnit.SECONDS);
            execWatch.close();
            System.out.println(out.toString());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ie.printStackTrace();
        }
    }
}