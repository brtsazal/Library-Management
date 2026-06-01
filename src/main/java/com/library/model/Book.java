package com.library.model;

/**
 * Represents a book in the library.
 */
public class Book extends LibraryItem {
    private String author; // The writer of the book
    private String isbn; // The unique ISBN number
    private int copies; // Total number of copies owned by the library

    public Book(String id, String title, String author, String isbn, int copies) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
    }

    public Book(String id, String title, String author, String isbn) {
        this(id, title, author, isbn, 1);
    }

    @Override
    public String getType() {
        return "Book";
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

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "Book [ID=" + id + ", Title=" + title + ", Author=" + author + ", ISBN=" + isbn + ", Copies=" + copies + "]";
    }
}
