package io.fabric8;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.SecurityContextConstraints;
import io.fabric8.openshift.api.model.SecurityContextConstraintsBuilder;

public class SecurityContextConstraintDemo {
	
	public static void main(String args[]) {
		KubernetesClient client = new DefaultKubernetesClient();
		
	    SecurityContextConstraints scc2 = new SecurityContextConstraintsBuilder()
	      .withNewMetadata().withName("scc1").endMetadata()
	      .withAllowPrivilegedContainer(true)
	      .withNewRunAsUser()
	      	.withType("RunAsAny")
	      .endRunAsUser()
	      .withNewSeLinuxContext()
	      	.withType("RunAsAny")
	      .endSeLinuxContext()
	      .withNewFsGroup()
	      	.withType("RunAsAny")
	      .endFsGroup()
	      .withNewSupplementalGroups()
	      	.withType("RunAsAny")
	      .endSupplementalGroups()
	      .addToUsers("admin")
	      .addToGroups("admin-group")
	      .build();

	    client.securityContextConstraints().createOrReplace(scc2);  // Doesn't work!
	    
	    client.close();
	}

}
