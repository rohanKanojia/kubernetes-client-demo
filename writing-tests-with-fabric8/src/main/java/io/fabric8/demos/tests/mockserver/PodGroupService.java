package io.fabric8.demos.tests.mockserver;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PodGroupService {
  private final KubernetesClient kubernetesClient;
  private final Map<String, String> matchLabels;

  public PodGroupService(KubernetesClient client, Map<String, String> matchLabels) {
    this.kubernetesClient = client;
    this.matchLabels = matchLabels;
  }

  public PodList list() {
    return kubernetesClient.pods().withLabels(matchLabels).list();
  }

  public int size() {
    return list().getItems().size();
  }

  public void addToGroup(Pod newPod) {
    Map<String, String> newLabels = new HashMap<>();
    Map<String, String> oldLabels = newPod.getMetadata().getLabels();
    newLabels.putAll(oldLabels);
    newLabels.putAll(matchLabels);

    newPod.getMetadata().setLabels(newLabels);
    kubernetesClient.pods().resource(newPod).serverSideApply();
  }

  public List<StatusDetails> delete() {
    return kubernetesClient.pods()
        .withLabels(matchLabels)
        .withGracePeriod(0L)
        .delete();
  }

  public Watch watch(Watcher<Pod> watcher) {
    return kubernetesClient.pods()
        .withLabels(matchLabels)
        .watch(watcher);
  }
}
