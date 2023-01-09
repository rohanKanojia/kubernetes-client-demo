package io.fabric8.demos.tests.e2e;

import io.fabric8.demos.tests.mockserver.PodGroupService;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.junit.jupiter.api.KubernetesTest;
import io.fabric8.junit.jupiter.api.RequireK8sSupport;
import io.fabric8.junit.jupiter.api.RequireK8sVersionAtLeast;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@KubernetesTest
@RequireK8sSupport(Pod.class)
@RequireK8sVersionAtLeast(majorVersion = 1, minorVersion = 16)
class PodGroupServiceIT {
  KubernetesClient kubernetesClient;


  @Test
  void list_whenPodsPresent_thenReturnList() {
    // Given
    Map<String, String> matchLabel = Collections.singletonMap("app", "list");
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, matchLabel);
    // When
    PodList podList = podGroupService.list();

    // Then
    assertNotNull(podList);
  }

  @Test
  void addToGroup_whenPodProvided_thenShouldUpdatePod() {
    // Given
    Map<String, String> matchLabel = Collections.singletonMap("app", "add-to-group");
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, matchLabel);
    Pod p1 = createNewPod("p1", "add-to-group");

    // When
    podGroupService.addToGroup(p1);

    // Then
    PodList podList = podGroupService.list();
    assertTrue(podList.getItems().stream().map(Pod::getMetadata).map(ObjectMeta::getName).anyMatch(n -> n.startsWith("p1")));
  }

  @Test
  void size_whenPodsAbsent_thenReturnZero() {
    // Given
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("app", "size-zero"));

    // When
    int result = podGroupService.size();

    // Then
    assertEquals(0, result);
  }

  @Test
  void size_whenPodsPresent_thenReturnActualSize() {
    // Given
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("app", "size-non-zero"));
    podGroupService.addToGroup(createNewPod("p1", "size-non-zero"));
    podGroupService.addToGroup(createNewPod("p2", "size-non-zero"));

    // When
    int result = podGroupService.size();

    // Then
    assertEquals(2, result);
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
      podGroupService.addToGroup(createNewPod("p1-watch", "watch-test"));
      assertTrue(eventReceivedLatch.await(5, TimeUnit.SECONDS));
    }
    // Then
    assertEquals(0, eventReceivedLatch.getCount());
  }

  @Test
  void delete_whenInvoked_shouldDeleteAllMatchingPods() {
    // Given
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("app", "delete-test"));
    podGroupService.addToGroup(createNewPod("p1", "delete-test"));
    podGroupService.addToGroup(createNewPod("p2", "delete-test"));

    // When
    List<StatusDetails> deleteStatusDetails = podGroupService.delete();
    int sizeAfterDelete = podGroupService.size();

    // Then
    assertEquals(2, deleteStatusDetails.size());
    assertEquals(0, sizeAfterDelete);
  }

  private Pod createNewPod(String generateName, String appLabelValue) {
    return new PodBuilder()
        .withNewMetadata().withGenerateName(generateName).withLabels(Collections.singletonMap("app", appLabelValue)).endMetadata()
        .withNewSpec()
        .addNewContainer()
        .withName("demo-container")
        .withImage("alpine:latest")
        .endContainer()
        .endSpec()
        .build();
  }
}
