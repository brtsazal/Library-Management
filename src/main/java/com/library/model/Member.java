package com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a library user with basic contact information.
 */
public class Member {
    private String memberId; // Unique ID for the member
    private String name; // Member's full name
    private String email; // Member's email address
    private List<Loan> borrowingHistory; // All loans ever associated with this member
    private List<Loan> currentLoans; // Active loans currently borrowed by this member

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowingHistory = new ArrayList<>();
        this.currentLoans = new ArrayList<>();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Loan> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public List<Loan> getCurrentLoans() {
        return Collections.unmodifiableList(currentLoans);
    }

    public void addToHistory(Loan loan) {
        borrowingHistory.add(loan);
    }

    public void addToCurrentLoans(Loan loan) {
        currentLoans.add(loan);
    }

    public void addLoan(Loan loan) {
        currentLoans.add(loan);
        borrowingHistory.add(loan);
    }

    public void removeLoan(Loan loan) {
        currentLoans.remove(loan);
    }

    @Override
    public String toString() {
        return "Member [ID=" + memberId + ", Name=" + name + ", Email=" + email
                + ", Current Loans=" + currentLoans.size() + "]";
    }
}
