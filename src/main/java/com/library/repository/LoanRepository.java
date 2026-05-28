package com.library.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.model.Loan;

/**
 * Stores and manages all the borrowing transactions (loans) in memory.
 */
public class LoanRepository implements Repository<Loan, String> {
    private List<Loan> loans; // The list of all loans

    public LoanRepository() {
        this.loans = new ArrayList<>();
    }

    @Override
    public void add(Loan item) {
        loans.add(item); // Add the new loan record
    }

    @Override
    public void remove(String id) {
        // Remove loan by ID
        loans.removeIf(loan -> loan.getLoanId().equals(id));
    }

    @Override
    public Optional<Loan> findById(String id) {
        // Find a specific loan
        return loans.stream().filter(loan -> loan.getLoanId().equals(id)).findFirst();
    }

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(loans); // Get all loan records
    }
}
