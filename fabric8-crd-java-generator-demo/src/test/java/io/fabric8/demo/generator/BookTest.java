package io.fabric8.demo.generator;

import io.fabric8.demo.generator.v1alpha1.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {
  @Test
  void testGeneratedBook() {
    // Given + When
    Book cronTab = new Book();

    // Then
    assertEquals("generator.demo.fabric8.io/v1alpha1", cronTab.getApiVersion());
    assertEquals("books", cronTab.getPlural());
    assertEquals("Namespaced", cronTab.getScope());
    assertEquals("Book", cronTab.getKind());
  }
}
