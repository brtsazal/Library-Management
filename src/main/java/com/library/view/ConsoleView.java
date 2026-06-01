package com.library.view;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrowing Operations");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }

    public void displayBookMenu() {
        System.out.println("\n--- Book Management ---");
        System.out.println("Initial menu structure created.");
        System.out.println("Feature implementation will be added later.");
        System.out.print("Select an option: ");
    }

    public void displayMemberMenu() {
        System.out.println("\n--- Member Management ---");
        System.out.println("Initial menu structure created.");
        System.out.println("Feature implementation will be added later.");
        System.out.print("Select an option: ");
    }

    public void displayBorrowingMenu() {
        System.out.println("\n--- Borrowing Operations ---");
        System.out.println("1. Borrow Book");
        System.out.println("2. Return Book");
        System.out.println("3. View Current Loans of a Member");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
    }

    public String getUserInput() {
        return scanner.nextLine().trim();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String error) {
        System.err.println("Error: " + error);
    }
}
