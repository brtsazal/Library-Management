package com.library.controller;

import com.library.model.Loan;
import com.library.model.Member;
import com.library.service.LibraryService;
import com.library.view.ConsoleView;

import java.util.List;

public class LibraryController {
    private final LibraryService service;
    private final ConsoleView view;

    public LibraryController(LibraryService service, ConsoleView view) {
        this.service = service;
        this.view = view;
    }

    public void start() {
        service.initialize();
        boolean running = true;

        while (running) {
            view.displayMainMenu();
            String choice = view.getUserInput();

            switch (choice) {
                case "1":
                    handleBookManagement();
                    break;
                case "2":
                    handleMemberManagement();
                    break;
                case "3":
                    handleBorrowingOperations();
                    break;
                case "0":
                    running = false;
                    view.displayMessage("Exiting system. Goodbye!");
                    break;
                default:
                    view.displayError("Invalid option. Try again.");
                    break;
            }
        }
    }

    private void handleBookManagement() {
        boolean back = false;
        while (!back) {
            view.displayBookMenu();
            String choice = view.getUserInput();
            try {
                switch (choice) {
                    case "1":
                        view.displayMessage("Enter Title:");
                        String title = view.getUserInput();
                        view.displayMessage("Enter Author:");
                        String author = view.getUserInput();
                        view.displayMessage("Enter ISBN:");
                        String isbn = view.getUserInput();
                        view.displayMessage("Enter Number of Copies:");
                        String copiesInput = view.getUserInput();

                        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || copiesInput.isEmpty()) {
                            view.displayError("Fields cannot be empty.");
                            break;
                        }

                        int copies;
                        try {
                            copies = Integer.parseInt(copiesInput);
                            if (copies <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            view.displayError("Number of copies must be a positive integer.");
                            break;
                        }

                        service.addBook(title, author, isbn, copies);
                        view.displayMessage("Book added successfully.");
                        break;
                    case "2":
                        view.displayMessage("Enter Book ID to update:");
                        String id = view.getUserInput();
                        service.getBook(id);
                        view.displayMessage("Enter New Title (leave blank to keep current):");
                        String newTitle = view.getUserInput();
                        view.displayMessage("Enter New Author (leave blank to keep current):");
                        String newAuthor = view.getUserInput();
                        view.displayMessage("Enter New ISBN (leave blank to keep current):");
                        String newIsbn = view.getUserInput();
                        service.updateBook(id, newTitle, newAuthor, newIsbn);
                        view.displayMessage("Book updated successfully.");
                        break;
                    case "3":
                        view.displayMessage("Enter Book ID to remove:");
                        String removeId = view.getUserInput();
                        service.removeBook(removeId);
                        view.displayMessage("Book removed successfully.");
                        break;
                    case "4":
                        view.displayMessage("Enter search query (Title, Author, or ISBN):");
                        String query = view.getUserInput();
                        List<com.library.model.Book> foundBooks = service.searchBooks(query);
                        if (foundBooks.isEmpty()) {
                            view.displayMessage("No books found.");
                        } else {
                            foundBooks.forEach(book -> view.displayMessage(book.toString()));
                        }
                        break;
                    case "5":
                        List<com.library.model.Book> allBooks = service.getAllBooks();
                        if (allBooks.isEmpty()) {
                            view.displayMessage("No books in the library.");
                        } else {
                            allBooks.forEach(book -> view.displayMessage(book.toString()));
                        }
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        view.displayError("Invalid option.");
                        break;
                }
            } catch (Exception e) {
                view.displayError(e.getMessage());
            }
        }
    }

    private void handleMemberManagement() {
        boolean back = false;
        while (!back) {
            view.displayMemberMenu();
            String choice = view.getUserInput();
            try {
                switch (choice) {
                    case "1":
                        view.displayMessage("Enter Member Name:");
                        String name = view.getUserInput();
                        view.displayMessage("Enter Member Email:");
                        String email = view.getUserInput();

                        if (name.isEmpty() || email.isEmpty()) {
                            view.displayError("Fields cannot be empty.");
                            break;
                        }

                        service.registerMember(name, email);
                        view.displayMessage("Member registered successfully.");
                        break;
                    case "2":
                        view.displayMessage("Enter Member ID to update:");
                        String id = view.getUserInput();
                        service.getMember(id);
                        view.displayMessage("Enter New Name (leave blank to keep current):");
                        String newName = view.getUserInput();
                        view.displayMessage("Enter New Email (leave blank to keep current):");
                        String newEmail = view.getUserInput();

                        service.updateMember(id, newName, newEmail);
                        view.displayMessage("Member updated successfully.");
                        break;
                    case "3":
                        view.displayMessage("Enter Member ID:");
                        String memberId = view.getUserInput();
                        Member member = service.getMember(memberId);
                        List<Loan> history = member.getBorrowingHistory();
                        if (history.isEmpty()) {
                            view.displayMessage("No borrowing history.");
                        } else {
                            view.displayMessage("Borrowing History for " + member.getName() + ":");
                            history.forEach(loan -> view.displayMessage(loan.toString()));
                        }
                        break;
                    case "4":
                        List<Member> allMembers = service.getAllMembers();
                        if (allMembers.isEmpty()) {
                            view.displayMessage("No members registered.");
                        } else {
                            allMembers.forEach(existingMember -> view.displayMessage(existingMember.toString()));
                        }
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        view.displayError("Invalid option.");
                        break;
                }
            } catch (Exception e) {
                view.displayError(e.getMessage());
            }
        }
    }

    private void handleBorrowingOperations() {
        boolean back = false;
        while (!back) {
            view.displayBorrowingMenu();
            String choice = view.getUserInput();

            try {
                switch (choice) {
                    case "1":
                        view.displayMessage("Enter Member ID:");
                        String memberId = view.getUserInput();
                        service.getMember(memberId);
                        view.displayMessage("Enter Book ID:");
                        String bookId = view.getUserInput();
                        service.getBook(bookId);
                        try {
                            service.borrowBook(memberId, bookId);
                            view.displayMessage("Book borrowed successfully.");
                        } catch (IllegalStateException e) {
                            if (e.getMessage().contains("waitlist")) {
                                view.displayMessage(e.getMessage());
                            } else {
                                view.displayError(e.getMessage());
                            }
                        }
                        break;
                    case "2":
                        view.displayMessage("Enter Member ID:");
                        String returnMemberId = view.getUserInput();
                        service.getMember(returnMemberId);
                        view.displayMessage("Enter Book ID:");
                        String returnBookId = view.getUserInput();
                        service.getBook(returnBookId);
                        String result = service.returnBook(returnMemberId, returnBookId);
                        view.displayMessage(result);
                        break;
                    case "3":
                        view.displayMessage("Enter Member ID:");
                        String currentMemberId = view.getUserInput();
                        Member member = service.getMember(currentMemberId);
                        List<Loan> currentLoans = member.getCurrentLoans();
                        if (currentLoans.isEmpty()) {
                            view.displayMessage("No current loans.");
                        } else {
                            view.displayMessage("Current Loans for " + member.getName() + ":");
                            currentLoans.forEach(loan -> view.displayMessage(loan.toString()));
                        }
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        view.displayError("Invalid option.");
                        break;
                }
            } catch (Exception e) {
                view.displayError(e.getMessage());
            }
        }
    }
}
