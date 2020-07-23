package io.fabric8.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;

public class CronTab extends CustomResource implements Namespaced {
    private CronTabSpec spec;
    private CronTabStatus status;

    @Override
    public ObjectMeta getMetadata() {
        return super.getMetadata();
    }

    public CronTabSpec getSpec() {
        return spec;
    }

    public void setSpec(CronTabSpec spec) {
        this.spec = spec;
    }

    public CronTabStatus getStatus() {
        return status;
    }

    public void setStatus(CronTabStatus status) {
        this.status = status;
    }

    @Override
    public String getApiVersion() {
        return "stable.example.com/v1";
    }

    @Override
    public String toString() {
        return "CronTab{"+
                "apiVersion='" + getApiVersion() + "'" +
                ", metadata=" + getMetadata() +
                ", spec=" + spec +
                ", status=" + status +
                "}";
    }
}
