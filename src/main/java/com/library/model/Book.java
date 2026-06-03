package com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a book in the library.
 */
public class Book extends LibraryItem {
    private String author; // The writer of the book
    private String isbn; // The unique ISBN number
    private int copies; // Total number of copies owned by the library
    private int availableCopies; // Current number of copies available for loan
    private List<Member> waitlist; // Members waiting for this book

    public Book(String id, String title, String author, String isbn, int copies) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
        this.availableCopies = copies;
        this.waitlist = new ArrayList<>();
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

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isBorrowed() {
        return availableCopies == 0;
    }

    public void setBorrowed(boolean borrowed) {
        this.availableCopies = borrowed ? 0 : this.copies;
    }

    public List<Member> getWaitlist() {
        return Collections.unmodifiableList(waitlist);
    }

    public void addToWaitlist(Member member) {
        if (!waitlist.contains(member)) {
            waitlist.add(member);
        }
    }

    public void removeFromWaitlist(Member member) {
        waitlist.remove(member);
    }

    @Override
    public String toString() {
        return "Book [ID=" + getId() + ", Title=" + getTitle() + ", Author=" + author + ", ISBN=" + isbn
                + ", Copies=" + copies + ", Available=" + availableCopies + ", Waitlist Size=" + waitlist.size() + "]";
    }
}
