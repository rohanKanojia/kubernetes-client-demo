package io.fabric8.programming.kubernetes.java.customresource;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.JSONSchemaPropsBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import static io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext.v1CRDFromCustomResourceType;

public class CreateBookCRDDemo {
  public static void main(String[] args) {
    try (KubernetesClient client = new KubernetesClientBuilder().build()) {
      CustomResourceDefinition crd = v1CRDFromCustomResourceType(Book.class)
          .editSpec().editVersion(0)
            .withNewSchema()
              .withNewOpenAPIV3Schema()
                .withType("object")
                .addToProperties("spec", new JSONSchemaPropsBuilder()
                    .withType("object")
                    .addToProperties("title", new JSONSchemaPropsBuilder()
                        .withType("string")
                        .build())
                    .addToProperties("author", new JSONSchemaPropsBuilder()
                        .withType("string")
                        .build())
                    .addToProperties("isbn", new JSONSchemaPropsBuilder()
                        .withType("string")
                        .build())
                    .build())
                .addToProperties("status", new JSONSchemaPropsBuilder()
                    .withType("object")
                    .addToProperties("issued", new JSONSchemaPropsBuilder()
                        .withType("boolean")
                        .build())
                    .addToProperties("issuedto", new JSONSchemaPropsBuilder()
                        .withType("string")
                        .build())
                    .build())
              .endOpenAPIV3Schema()
            .endSchema()
          .endVersion().endSpec().build();

      client.apiextensions().v1()
          .customResourceDefinitions()
          .resource(crd)
          .create();
    }
  }
}
