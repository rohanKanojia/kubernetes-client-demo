package io.fabric8;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.utils.Serialization;

public class PodExecGetCommandStatusCode {
  public static void main(String[] args) {
    try (KubernetesClient client = new DefaultKubernetesClient()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Pod pod = client.pods()
          .load(PodExecGetCommandStatusCode.class.getResourceAsStream("/test-pod.yml"))
          .get();

      client.pods().inNamespace("default").create(pod);

      ExecWatch execWatch = client.pods().inNamespace("default").withName("example-pod")
          .writingOutput(System.out)
          .writingError(System.out)
          .writingErrorChannel(baos)
          .exec("/bin/ping", "goo");
      Thread.sleep(5 * 1000L);

      System.out.println(baos);
      Status execStatus = Serialization.jsonMapper().readValue(baos.toString(), Status.class);
      if (execStatus.getStatus().equals("Success")) {
        System.out.println("ExitCode 0");
      } else {
        System.out.println(execStatus.getDetails().getCauses().get(0).getReason() + " " +
            execStatus.getDetails().getCauses().get(0).getMessage());
      }
      execWatch.close();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
