package io.fabric8.openshift;

import io.fabric8.openshift.api.model.RoleBinding;
import io.fabric8.openshift.api.model.RoleBindingBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class OpenShiftRoleBindingCreate {
    public static void main(String[] args) {
        try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient()) {
            RoleBinding roleBinding = new RoleBindingBuilder()
                    .withNewMetadata().withName("myownrolebinding").endMetadata()
                    .withNewRoleRef()
                    .withName("machine-config-controller-events")
                    .endRoleRef()
                    .addNewSubject()
                    .withKind("ServiceAccount")
                    .withName("build-robot")
                    .withNamespace("default")
                    .endSubject()
                    .build();

            openShiftClient.roleBindings().inNamespace("default").create(roleBinding);
        }
    }
}
