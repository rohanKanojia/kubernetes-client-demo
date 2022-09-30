package io.fabric8.programming.kubernetes.java.customresource;

import io.fabric8.crd.model.v1alpha1.Book;
import io.fabric8.crd.model.v1alpha1.BookSpec;
import io.fabric8.crd.model.v1alpha1.BookStatus;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

public class BookUtil {
  private BookUtil() { }
  public static Book createNewBook(String name, String title, String author, String isbn) {
    BookSpec bookSpec = new BookSpec();
    bookSpec.setTitle(title);
    bookSpec.setAuthor(author);
    bookSpec.setIsbn(isbn);
    Book book = new Book();
    book.setMetadata(new ObjectMetaBuilder()
        .withName(name)
        .build());
    book.setSpec(bookSpec);
    return book;
  }

  public static BookStatus createBookStatus(boolean isIssued, String issuedTo) {
    BookStatus bookStatus = new BookStatus();
    bookStatus.setIssued(isIssued);
    bookStatus.setIssuedto(issuedTo);
    return bookStatus;
  }
}
