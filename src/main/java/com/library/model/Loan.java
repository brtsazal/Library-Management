package com.library.model;

import java.time.LocalDate;

/**
 * Represents a basic loan record for a borrowed book.
 */
public class Loan {
    private String loanId; // Unique ID for this loan transaction
    private Book book; // The book being borrowed
    private Member member; // The member who borrowed the book
    private LocalDate borrowDate; // Date the book was borrowed
    private LocalDate dueDate; // Date the book is due
    private LocalDate returnDate; // Date the book was returned (may be null)

    public Loan(String loanId, Book book, Member member, LocalDate borrowDate, LocalDate dueDate) {
        this.loanId = loanId;
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
