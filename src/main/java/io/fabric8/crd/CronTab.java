package io.fabric8.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("stable.example.com")
@Version("v1")
public class CronTab extends CustomResource<CronTabSpec, CronTabStatus> implements Namespaced {
}
