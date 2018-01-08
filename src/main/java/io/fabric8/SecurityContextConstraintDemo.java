package io.fabric8;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.SecurityContextConstraints;
import io.fabric8.openshift.api.model.SecurityContextConstraintsBuilder;

public class SecurityContextConstraintDemo {

	private static final Logger logger = Logger
			.getLogger(SecurityContextConstraintDemo.class.getName());
	
	public static void main(String args[]) {
		KubernetesClient client = new DefaultKubernetesClient();

		SecurityContextConstraints scc2 = new SecurityContextConstraintsBuilder()
				.withApiVersion("v1").withNewMetadata().withName("scc1")
				.endMetadata().withAllowPrivilegedContainer(true)
				.withNewRunAsUser().withType("RunAsAny").endRunAsUser()
				.withNewSeLinuxContext().withType("RunAsAny")
				.endSeLinuxContext().withNewFsGroup().withType("RunAsAny")
				.endFsGroup().withNewSupplementalGroups().withType("RunAsAny")
				.endSupplementalGroups().addToUsers("admin")
				.addToGroups("admin-group").build();

		client.securityContextConstraints().createOrReplace(scc2);
		logger.log(Level.INFO, "successfully created scc");

		client.close();
	}

}
