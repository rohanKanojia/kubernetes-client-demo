package io.fabric8.crd;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;

@JsonDeserialize(
        using = JsonDeserializer.None.class
)
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
