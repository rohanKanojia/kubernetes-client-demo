package io.fabric8.demos.tests.mockserver;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodListBuilder;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.WatchEvent;
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

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableKubernetesMockClient
class PodGroupServiceTest {
  private KubernetesClient kubernetesClient;
  private KubernetesMockServer server;

  @Test
  void list_whenPodsPresent_thenReturnList() {
    // Given
    server.expect().get()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar")
        .andReturn(HTTP_OK, new PodListBuilder().build())
        .once();
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
    server.expect().post()
        .withPath("/api/v1/namespaces/test/pods")
        .andReturn(HTTP_OK, p1)
        .once();
    server.expect().get()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar")
        .andReturn(HTTP_OK, new PodListBuilder().addToItems(p1).build())
        .once();

    // When
    podGroupService.addToGroup(p1);

    // Then
    PodList podList = podGroupService.list();
    assertTrue(podList.getItems().stream().map(Pod::getMetadata).map(ObjectMeta::getName).anyMatch(n -> n.equals("p1")));
  }

  @Test
  void size_whenPodsWithLabelPresent_thenReturnCount() {
    // Given
    server.expect().get()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar")
        .andReturn(HTTP_OK, new PodListBuilder().addToItems(
                new PodBuilder()
                    .withNewMetadata()
                    .withName("pod1")
                    .addToLabels("foo", "bar")
                    .endMetadata()
                    .build())
            .build())
        .once();
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("foo", "bar"));

    // When
    int count = podGroupService.size();

    // Then
    assertEquals(1, count);
  }

  @Test
  void size_whenPodsWithLabelAbsent_thenReturnZero() {
    // Given
    server.expect().get()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar")
        .andReturn(HTTP_OK, new PodListBuilder().build())
        .once();
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("foo", "bar"));

    // When
    int count = podGroupService.size();

    // Then
    assertEquals(0, count);
  }

  @Test
  void watch_whenInvoked_shouldMonitorUpdates() throws Exception {
    // Given
    Pod p1 = createNewPod("p1", Collections.singletonMap("foo", "bar"));
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("foo", "bar"));
    CountDownLatch eventReceivedLatch = new CountDownLatch(1);
    server.expect()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar&allowWatchBookmarks=true&watch=true")
        .andUpgradeToWebSocket()
        .open()
        .waitFor(50)
        .andEmit(new WatchEvent(p1, "ADDED"))
        .done()
        .once();
    server.expect().post()
        .withPath("/api/v1/namespaces/test/pods")
        .andReturn(HTTP_OK, p1)
        .once();

    // When
    try (Watch ignore = podGroupService.watch(new Watcher<>() {
      @Override
      public void eventReceived(Action action, Pod pod) {
        eventReceivedLatch.countDown();
      }

      @Override
      public void onClose(WatcherException e) { }
    })) {
      podGroupService.addToGroup(p1);
      assertTrue(eventReceivedLatch.await(5, TimeUnit.SECONDS));
    }
    // Then
    assertEquals(0, eventReceivedLatch.getCount());
  }

  @Test
  void delete_whenInvoked_shouldDeleteAllMatchingPods() {
    // Given
    Pod p1 = createNewPod("p1", Collections.singletonMap("f1", "b1"));
    Pod p2 = createNewPod("p2", Collections.emptyMap());
    server.expect().post()
        .withPath("/api/v1/namespaces/test/pods")
        .andReturn(HTTP_OK, p1)
        .once();
    server.expect().post()
        .withPath("/api/v1/namespaces/test/pods")
        .andReturn(HTTP_OK, p2)
        .once();
    server.expect().delete()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar")
        .andReturn(HTTP_OK, new PodListBuilder().addToItems(p1, p2).build())
        .once();
    server.expect().get()
        .withPath("/api/v1/namespaces/test/pods?labelSelector=foo%3Dbar")
        .andReturn(HTTP_OK, new PodListBuilder().build())
        .once();
    PodGroupService podGroupService = new PodGroupService(kubernetesClient, Collections.singletonMap("foo", "bar"));
    podGroupService.addToGroup(p1);
    podGroupService.addToGroup(p2);

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
