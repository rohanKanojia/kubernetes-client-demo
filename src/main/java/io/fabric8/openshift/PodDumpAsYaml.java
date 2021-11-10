package io.fabric8.openshift;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.internal.SerializationUtils;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class PodDumpAsYaml {
  public static void main(String[] args) {
    try (OpenShiftClient oClient = new DefaultOpenShiftClient()) {
      String npName = "rokumar-dev";
      String podName = "pod-with-annotation";
      Pod pod = oClient.pods().inNamespace(npName).withName(podName).get();
      //String yaml = SerializationUtils.dumpAsYaml(pod);
      String yaml = Serialization.yamlMapper().writeValueAsString(pod);
      System.out.println(yaml);
    } catch (JsonProcessingException jsonProcessingException) {
      jsonProcessingException.printStackTrace();
    }
  }
}
