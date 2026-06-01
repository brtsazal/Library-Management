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
                    view.displayBookMenu();
                    view.displayMessage("Book management setup is ready.");
                    break;
                case "2":
                    view.displayMemberMenu();
                    view.displayMessage("Member management setup is ready.");
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
