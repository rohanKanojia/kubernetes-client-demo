package io.fabric8.demo.crd.v1alpha1;

public class BookSpec {
    private String title;
    private String author;
    private String isbn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
//
//    @Override
//    public String toString() {
//        return "BookSpec{title=" + title + "," +
//                "author=" + author + "," +
//                "isbn=" + isbn + "}";
//    }
}
