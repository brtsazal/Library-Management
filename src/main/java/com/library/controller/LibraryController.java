package com.library.controller;

import com.library.service.LibraryService;
import com.library.view.ConsoleView;

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
                    view.displayBorrowingMenu();
                    view.displayMessage("Borrowing operations setup is ready.");
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
}
