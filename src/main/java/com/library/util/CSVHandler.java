package com.library.util;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.MemberRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Utility class to save and load repository data using CSV files.
 */
public class CSVHandler {
    private static final String BOOKS_FILE = "books.csv";
    private static final String MEMBERS_FILE = "members.csv";
    private static final String LOANS_FILE = "loans.csv";
    private static final String WISHLIST_FILE = "wishlist.csv";

    public static void saveData(BookRepository bookRepo, MemberRepository memberRepo, LoanRepository loanRepo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            writer.println("ID,Title,Author,ISBN,Copies,AvailableCopies");
            for (Book book : bookRepo.findAll()) {
                String title = book.getTitle().replace(",", "");
                String author = book.getAuthor().replace(",", "");
                writer.println(String.join(",", book.getId(), title, author, book.getIsbn(),
                        String.valueOf(book.getCopies()), String.valueOf(book.getAvailableCopies())));
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(MEMBERS_FILE))) {
            writer.println("ID,Name,Email");
            for (Member member : memberRepo.findAll()) {
                String name = member.getName().replace(",", "");
                writer.println(String.join(",", member.getMemberId(), name, member.getEmail()));
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOANS_FILE))) {
            writer.println("LoanID,BookID,MemberID,BorrowDate,DueDate,ReturnDate");
            for (Loan loan : loanRepo.findAll()) {
                writer.println(String.join(",", loan.getLoanId(), loan.getBook().getId(), loan.getMember().getMemberId(),
                        loan.getBorrowDate().toString(), loan.getDueDate().toString(),
                        loan.getReturnDate() != null ? loan.getReturnDate().toString() : ""));
            }
        } catch (IOException e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(WISHLIST_FILE))) {
            writer.println("BookID,MemberID");
            for (Book book : bookRepo.findAll()) {
                for (Member member : book.getWaitlist()) {
                    writer.println(book.getId() + "," + member.getMemberId());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving wishlist: " + e.getMessage());
        }
    }

    public static void loadData(BookRepository bookRepo, MemberRepository memberRepo, LoanRepository loanRepo) {
        File membersFile = new File(MEMBERS_FILE);
        if (membersFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(membersFile))) {
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 3) {
                        memberRepo.add(new Member(parts[0], parts[1], parts[2]));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading members: " + e.getMessage());
            }
        }

        File booksFile = new File(BOOKS_FILE);
        if (booksFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(booksFile))) {
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 6) {
                        int copies = Integer.parseInt(parts[4]);
                        int availableCopies = Integer.parseInt(parts[5]);
                        Book book = new Book(parts[0], parts[1], parts[2], parts[3], copies);
                        book.setAvailableCopies(availableCopies);
                        bookRepo.add(book);
                    } else if (parts.length == 5) {
                        boolean oldBorrowed = Boolean.parseBoolean(parts[4]);
                        Book book = new Book(parts[0], parts[1], parts[2], parts[3], 1);
                        book.setAvailableCopies(oldBorrowed ? 0 : 1);
                        bookRepo.add(book);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading books: " + e.getMessage());
            }
        }

        File loansFile = new File(LOANS_FILE);
        if (loansFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(loansFile))) {
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 6) {
                        String loanId = parts[0];
                        String bookId = parts[1];
                        String memberId = parts[2];
                        LocalDate borrowDate = LocalDate.parse(parts[3]);
                        LocalDate dueDate = LocalDate.parse(parts[4]);
                        LocalDate returnDate = parts[5].isEmpty() ? null : LocalDate.parse(parts[5]);

                        Book book = bookRepo.findById(bookId).orElse(null);
                        Member member = memberRepo.findById(memberId).orElse(null);

                        if (book != null && member != null) {
                            Loan loan = new Loan(loanId, book, member, borrowDate, dueDate);
                            if (returnDate != null) {
                                loan.setReturnDate(returnDate);
                            }
                            loanRepo.add(loan);
                            member.addToHistory(loan);
                            if (returnDate == null) {
                                member.addToCurrentLoans(loan);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading loans: " + e.getMessage());
            }
        }

        File wishlistFile = new File(WISHLIST_FILE);
        if (wishlistFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(wishlistFile))) {
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 2) {
                        Book book = bookRepo.findById(parts[0]).orElse(null);
                        Member member = memberRepo.findById(parts[1]).orElse(null);
                        if (book != null && member != null) {
                            book.addToWaitlist(member);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading wishlist: " + e.getMessage());
            }
        }
    }
}
