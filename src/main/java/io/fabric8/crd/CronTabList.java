package io.fabric8.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResourceList;

public class CronTabList extends CustomResourceList<CronTab> implements Namespaced {
}
