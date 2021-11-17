package io.fabric8;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;

import java.io.File;
import java.io.IOException;

public class GenericKubernetesResourceNoContextExample {
  public static void main(String[] args) {
    try (KubernetesClient client = new DefaultKubernetesClient()) {
      File fragment = new File(GenericKubernetesResourceNoContextExample.class.getResource("/crontab.yml").getFile());
      GenericKubernetesResource cr = Serialization.yamlMapper().readValue(fragment, GenericKubernetesResource.class);

      client.genericKubernetesResources("stable.example.com/v1", "CronTab").inNamespace("default").create(cr);


    } catch (StreamReadException e) {
      e.printStackTrace();
    } catch (DatabindException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
