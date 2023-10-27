package io.fabric8.java.crd.generator;

import io.fabric8.demo.crd.v1alpha1.Book;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionVersion;
import io.fabric8.kubernetes.client.utils.KubernetesSerialization;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookGeneratedYamlDeserializeTest {
  @Test
  void generatedManifestShouldBeDeserializable() {
    // Given
    Book book = new Book();
    KubernetesSerialization kubernetesSerialization = new KubernetesSerialization();

    // When
    CustomResourceDefinition crd = kubernetesSerialization.unmarshal(getClass().getResourceAsStream("/META-INF/fabric8/books.testing.fabric8.io-v1.yml"));

    // Then
    assertNotNull(crd);
    assertEquals(book.getGroup(), crd.getSpec().getGroup());
    assertTrue(crd.getSpec().getVersions().stream().map(CustomResourceDefinitionVersion::getName).anyMatch(n -> n.equals(book.getVersion())));
    assertEquals(book.getScope(), crd.getSpec().getScope());
    assertEquals(book.getKind(), crd.getSpec().getNames().getKind());
    assertEquals(book.getGroup(), crd.getSpec().getGroup());
  }
}
