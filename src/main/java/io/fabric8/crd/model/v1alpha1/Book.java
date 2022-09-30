package io.fabric8.crd.model.v1alpha1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1alpha1")                             // -> CRD Version
@Group("testing.fabric8.io")                     // -> CRD Group
public class Book                                // -> CRD Kind (if not provided in @Kind annotation)
    extends CustomResource<BookSpec, BookStatus> // -> .spec -> BookSpec , .status -> BookStatus
    implements Namespaced { }                    // -> CRD scope Namespaced
