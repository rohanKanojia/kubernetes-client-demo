package io.fabric8.crd;

import io.fabric8.kubernetes.api.model.KubernetesResource;

public class CronTabStatus implements KubernetesResource  {
    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    private int replicas;

    public String getLabelSelector() {
        return labelSelector;
    }

    public void setLabelSelector(String labelSelector) {
        this.labelSelector = labelSelector;
    }

    private String labelSelector;

    @Override
    public String toString() {
        return "CronTabStatus{" +
                " replicas=" + replicas +
                " , labelSelector='" + labelSelector + "'" +
                "}";
    }
}
