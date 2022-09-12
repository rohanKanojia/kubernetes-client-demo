package io.fabric8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceList;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.json.JSONException;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RawCustomResourcesDemoBlog {
    private static final Logger logger = Logger.getLogger(RawCustomResourcesDemoBlog.class.getName());
    private static final CountDownLatch closeLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        try (final KubernetesClient client = new KubernetesClientBuilder().build()) {
            String namespace = "default";

            // Load CRD as object from YAML
            CustomResourceDefinition animalCrd = client.apiextensions().v1()
                .customResourceDefinitions()
                .load(RawCustomResourcesDemoBlog.class.getResourceAsStream("/animals-crd.yml"))
                .get();
            // Apply CRD object onto your Kubernetes cluster
            client.apiextensions().v1().customResourceDefinitions().resource(animalCrd).create();

            CustomResourceDefinitionContext animalCrdContext = new CustomResourceDefinitionContext.Builder()
                .withName("animals.jungle.example.com")
                .withGroup("jungle.example.com")
                .withScope("Namespaced")
                .withVersion("v1")
                .withPlural("animals")
                .build();

            // Creating from Yaml file
            GenericKubernetesResource cr1 = client
                .genericKubernetesResources(animalCrdContext)
                .load(RawCustomResourcesDemoBlog.class.getResourceAsStream("/seal-cr.yml"))
                .get();
            client.genericKubernetesResources(animalCrdContext).inNamespace(namespace).resource(cr1).create();

            // Creating using Builder
            GenericKubernetesResource cr2 = new GenericKubernetesResourceBuilder()
                .withApiVersion("jungle.example.com/v1")
                .withKind("Animal")
                .withNewMetadata().withName("bison").endMetadata()
                .withAdditionalProperties(Collections.singletonMap("spec", Collections.singletonMap("image", "strong-bison-image")))
                .build();
            client.genericKubernetesResources(animalCrdContext).inNamespace(namespace).resource(cr2).create();

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
            GenericKubernetesResource cr3 = Serialization.jsonMapper().readValue(crBasicString, GenericKubernetesResource.class);
            client.genericKubernetesResources(animalCrdContext).inNamespace(namespace).resource(cr3).create();


            // Listing Custom resources in a specific namespace
            GenericKubernetesResourceList animalList = client.genericKubernetesResources(animalCrdContext).inNamespace(namespace).list();
            for (GenericKubernetesResource animal : animalList.getItems()) {
                log(animal.getMetadata().getName());
            }

            // Updating a specific custom resource
            GenericKubernetesResource oldAnimal = client
                .genericKubernetesResources(animalCrdContext)
                .inNamespace(namespace)
                .withName("mongoose")
                .get();
            oldAnimal.setAdditionalProperties(Collections.singletonMap("spec", Collections.singletonMap("image", "my-silly-mongoose-image:v2")));
            oldAnimal.getMetadata().setLabels(Collections.singletonMap("updated", "true"));

            // Update using replace
            client.genericKubernetesResources(animalCrdContext)
                .inNamespace(namespace)
                .resource(oldAnimal)
                .replace();

            // Update using patch
            client.genericKubernetesResources(animalCrdContext)
                .inNamespace(namespace)
                .resource(oldAnimal)
                .patch(PatchContext.of(PatchType.JSON_MERGE),
                    "{\"metadata\":{\"labels\":{\"updated\":\"true\"}},\"spec\":{\"image\":\"my-silly-mongoose-image\"}}");

            // Deleting a custom resource
            client.genericKubernetesResources(animalCrdContext).inNamespace(namespace).withName("seal").delete();

            // Watching a custom resource
            logger.info("Watching custom resources now, open for 10 minutes...");
            Watch watch = client.genericKubernetesResources(animalCrdContext).inNamespace(namespace).watch(new Watcher<>() {
                @Override
                public void eventReceived(Action action, GenericKubernetesResource resource) {
                    try {
                        log(action + " : " + resource.getMetadata().getName());
                    } catch (JSONException exception) {
                        log("failed to parse object");
                    }
                }

                @Override
                public void onClose() {
                    log("Watch onClose");
                }

                @Override
                public void onClose(WatcherException e) {
                    log("Watcher onClose with exception");
                    closeLatch.countDown();
                    if (e != null) {
                        log(e.getMessage());
                    }
                }
            });
            boolean latchTerminationStatus = closeLatch.await(10, TimeUnit.MINUTES);
            if (!latchTerminationStatus) {
                log("Timed out");
            }
            watch.close();
        } catch (JsonMappingException e) {
            throw new RuntimeException("encountered JsonMappingException ", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
