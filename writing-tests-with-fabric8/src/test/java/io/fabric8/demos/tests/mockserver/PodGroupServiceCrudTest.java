package io.fabric8.demos.tests.mockserver;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableKubernetesMockClient(crud = true)
class PodGroupServiceCrudTest {
  private KubernetesClient kubernetesClient;
  private KubernetesMockServer server;

  @Test
  void list_whenPodsPresent_thenReturnList() {
    // Given
    Map<String, String> matchLabel = Collections.singletonMap("foo", "bar");
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, matchLabel);
    // When
    PodList podList = podGroupService.list();

    // Then
    assertNotNull(podList);
  }

  @Test
  void addToGroup_whenPodProvided_thenShouldUpdatePod() {
    // Given
    Map<String, String> matchLabel = Collections.singletonMap("foo", "bar");
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, matchLabel);
    Pod p1 = createNewPod("p1", Collections.singletonMap("other", "label"));

    // When
    podGroupService.addToGroup(p1);

    // Then
    PodList podList = podGroupService.list();
    assertTrue(podList.getItems().stream().map(Pod::getMetadata).map(ObjectMeta::getName).anyMatch(n -> n.equals("p1")));
  }

  @Test
  void size_whenPodsWithLabelPresent_thenReturnCount() {
    // Given
    Map<String, String> matchLabel = Collections.singletonMap("foo", "bar");
    kubernetesClient.pods().resource(createNewPod("p1", matchLabel)).create();
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, matchLabel);

    // When
    int count = podGroupService.size();

    // Then
    assertEquals(1, count);
  }

  @Test
  void size_whenPodsWithLabelAbsent_thenReturnZero() {
    // Given
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("foo", "bar"));

    // When
    int count = podGroupService.size();

    // Then
    assertEquals(0, count);
  }

  @Test
  void watch_whenInvoked_shouldMonitorUpdates() throws Exception {
    // Given
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("app", "watch-test"));
    CountDownLatch eventReceivedLatch = new CountDownLatch(1);

    // When
    try (Watch ignore = podGroupService.watch(new Watcher<>() {
      @Override
      public void eventReceived(Action action, Pod pod) {
        eventReceivedLatch.countDown();
      }

      @Override
      public void onClose(WatcherException e) { }
    })) {
      podGroupService.addToGroup(createNewPod("p1", Collections.singletonMap("foo", "bar")));
      assertTrue(eventReceivedLatch.await(5, TimeUnit.SECONDS));
    }
    // Then
    assertEquals(0, eventReceivedLatch.getCount());
  }

  @Test
  void delete_whenInvoked_shouldDeleteAllMatchingPods() {
    // Given
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("foo", "bar"));
    podGroupService.addToGroup(createNewPod("p1", Collections.singletonMap("f1", "b1")));
    podGroupService.addToGroup(createNewPod("p2", Collections.emptyMap()));

    // When
    List<StatusDetails> deleteStatusDetails = podGroupService.delete();
    int sizeAfterDelete = podGroupService.size();

    // Then
    assertEquals(2, deleteStatusDetails.size());
    assertEquals(0, sizeAfterDelete);
  }

  private Pod createNewPod(String name, Map<String, String> labels) {
    return new PodBuilder()
        .withNewMetadata()
        .withName(name)
        .addToLabels(labels)
        .endMetadata()
        .build();
  }
}
