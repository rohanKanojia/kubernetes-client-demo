/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class DeploymentRolloutExample {
  private static final Logger logger = LoggerFactory.getLogger(DeploymentRolloutExample.class);

  public static void main(String[] args) {
    try (KubernetesClient client = new DefaultKubernetesClient()) {
      updateImage(client);
      restart(client);
      pause(client);
      resume(client);
      undo(client);
      updateImage(client, Collections.singletonMap("nginx", "nginx:perl"));
      System.exit(0);
    }
  }

  private static void updateImage(KubernetesClient client) {
    client.apps().deployments()
      .inNamespace("default")
      .withName("nginx-deployment")
      .rolling()
      .updateImage("nginx:1.19");
  }

  private static void updateImage(KubernetesClient client, Map<String, String> params) {
    client.apps().deployments()
      .inNamespace("default")
      .withName("nginx-deployment")
      .rolling()
      .updateImage(params);
  }

  private static void restart(KubernetesClient client) {
    client.apps().deployments()
      .inNamespace("default")
      .withName("nginx-deployment")
      .rolling()
      .restart();
  }

  private static void pause(KubernetesClient client) {
    client.apps().deployments()
      .inNamespace("default")
      .withName("nginx-deployment")
      .rolling()
      .pause();
  }

  private static void resume(KubernetesClient client) {
    client.apps().deployments()
      .inNamespace("default")
      .withName("nginx-deployment")
      .rolling()
      .resume();
  }

  private static void undo(KubernetesClient client) {
    client.apps().deployments()
      .inNamespace("default")
      .withName("nginx-deployment")
      .rolling()
      .undo();
  }
}
