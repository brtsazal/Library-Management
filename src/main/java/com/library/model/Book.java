package com.library.model;

/**
 * Represents a book in the library with basic metadata.
 */
public class Book {
    private String id; // Unique ID for the book
    private String title; // Title of the book
    private String author; // The writer of the book
    private String isbn; // The unique ISBN number
    private int copies; // Total number of copies owned by the library

    public Book(String id, String title, String author, String isbn, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
    }

    public Book(String id, String title, String author, String isbn) {
        this(id, title, author, isbn, 1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
