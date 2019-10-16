package io.fabric8;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RawCustomResourcesDemoBlog {
    private static final Logger logger = Logger.getLogger(RawCustomResourcesDemoBlog.class.getName());
    private static final CountDownLatch closeLatch = new CountDownLatch(1);

    public static void main(String args[]) throws IOException, InterruptedException {

        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            String namespace = "default";

            // Load CRD as object from YAML
            CustomResourceDefinition animalCrd = client.customResourceDefinitions()
                    .load(RawCustomResourcesDemoBlog.class.getResourceAsStream("/animals-crd.yml")).get();
            // Apply CRD object onto your Kubernetes cluster
            client.customResourceDefinitions().create(animalCrd);

            CustomResourceDefinitionContext animalCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("animals.jungle.example.com")
                    .withGroup("jungle.example.com")
                    .withScope("Namespaced")
                    .withVersion("v1")
                    .withPlural("animals")
                    .build();

            // Creating from HashMap
            Map<String, Object> cr1 = client
                    .customResource(animalCrdContext)
                    .load(RawCustomResourcesDemoBlog.class.getResourceAsStream("/seal-cr.yml"));
            client.customResource(animalCrdContext).create(namespace, cr1);

            // Creating from JSON String
            JSONObject cr2Json = new JSONObject(cr1);
            cr2Json.getJSONObject("metadata").put("name", "bison");
            cr2Json.getJSONObject("spec").put("image", "strong-bison-image");

            client.customResource(animalCrdContext).create(namespace, cr2Json.toString());

            // Creating from Raw JSON String
            String crBasicString = "{" +
                    "  \"apiVersion\": \"jungle.example.com/v1\"," +
                    "  \"kind\": \"Animal\"," +
                    "  \"metadata\": {" +
                    "    \"name\": \"mongoose\"," +
                    "    \"namespace\": \"default\"" +
                    "  }," +
                    "  \"spec\": {" +
                    "    \"image\": \"my-silly-mongoose-image\"" +
                    "  }" +
                    "}";
            client.customResource(animalCrdContext).create(namespace, crBasicString);


            // Listing Custom resources in a specific namespace
            JSONObject animalListJSON = new JSONObject(client
                    .customResource(animalCrdContext)
                    .list(namespace));

            JSONArray animalItems = animalListJSON.getJSONArray("items");
            for (int index = 0; index < animalItems.length(); index++) {
                JSONObject currentItem = animalItems.getJSONObject(index);
                log(currentItem.getJSONObject("metadata").getString("name"));
            }

            // Updating a specific custom resource
            JSONObject oldAnimal = new JSONObject(client
                    .customResource(animalCrdContext)
                    .get(namespace, "mongoose"));
            oldAnimal.getJSONObject("spec")
                    .put("image", "my-silly-mongoose-image:v2");
            oldAnimal.getJSONObject("metadata")
                    .put("labels", new JSONObject("{\"updated\":\"true\"}"));

            client.customResource(animalCrdContext).edit(namespace, "mongoose", oldAnimal.toString());

            // Deleting a custom resource
            client.customResource(animalCrdContext).delete(namespace, "seal");

            // Watching a custom resource
            logger.info("Watching custom resources now, open for 10 minutes...");
            client.customResource(animalCrdContext).watch(namespace, new Watcher<String>() {
                @Override
                public void eventReceived(Action action, String resource) {
                    try {
                        JSONObject json = new JSONObject(resource);

                        log(action + " : " + json.getJSONObject("metadata").getString("name"));
                    } catch (JSONException exception) {
                        log("failed to parse object");
                    }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                    log("Watcher onClose");
                    closeLatch.countDown();
                    if (e != null) {
                        log(e.getMessage());
                    }
                }
            });
            closeLatch.await(10, TimeUnit.MINUTES);
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
