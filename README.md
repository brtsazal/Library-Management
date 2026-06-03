
# Library Management System

A highly robust, professional **Library Management System** implemented in Java, showcasing clean code practices and solid **Object-Oriented Programming (OOP)** design patterns. It features a responsive Command Line Interface (CLI) following the **Model-View-Controller (MVC)** architectural pattern, persistent local CSV storage, and an automated test suite.

---

## 🌟 Core Features

### 1. Book Asset Management & Multiple Copies
* **Asset Tracking**: Track books by ID, Title, Author, and ISBN.
* **Multiple Copies Support**: Instead of adding duplicate entries for the same title, the library tracks both `Copies` (total owned) and `Available Copies` in a single record.
* **Waitlist (Group 3 Extension)**: If all copies of a book are currently borrowed, members can join a waitlist. Returning a copy automatically makes it available to the next member in line.

### 2. Member & Loan Management
* **Member Profiles**: Track member registration, active loans, and borrowing history.
* **Borrowing Rules**: Members can borrow up to a maximum of **3 books** concurrently for a default period of **14 days**.
* **Loan Logging**: Tracks the borrow date, due date, and actual return date of every transaction.

### 3. Data Integrity & Input Validation Guards
* **Active Loan Removal Guard**: The system prevents library administrators from deleting a book record if any of its copies are currently borrowed, ensuring no dangling loan records are created.
* **Duplicate ISBN Enforcement**: Enforces international standard uniqueness by rejecting additions or updates of books that share an ISBN with an existing record.
* **Email Format Validation**: Rejects invalid email strings (ensures they contain at least `@` and `.`) during member registration and updates.

### 4. Resilient Local Data Storage
* **CSV Database**: All data (`books.csv`, `members.csv`, `loans.csv`, and `wishlist.csv`) is automatically loaded on startup and synchronized on exit.
* **Schema Backward Compatibility**: The CSV reader features a robust fallback parser. If older-format CSV files (without copies columns) are detected, the system automatically migrates them to the multi-copy schema without data loss or crashes.

---

## 🏗️ Technical Architecture & OOP Principles

The application is structured around a clean **Model-View-Controller (MVC)** architecture to separate concerns, making it highly modular, maintainable, and testable.

### Applied OOP Principles
* **Strict Encapsulation**: Members control their own loan lists. Getters for lists return unmodifiable views (`Collections.unmodifiableList`), and list updates are strictly controlled through cohesive domain methods (`addToHistory(loan)` / `addToCurrentLoans(loan)`).
* **Inheritance & Abstraction**: [LibraryItem](file:///c:/Users/ACER/Desktop/Library_management_System/src/main/java/com/library/model/LibraryItem.java) is a true abstract parent class declaring the abstract method `public abstract String getType();`, which is implemented by subclasses (such as [Book](file:///c:/Users/ACER/Desktop/Library_management_System/src/main/java/com/library/model/Book.java)) to provide polymorphic type identification.
* **Polymorphism**: The data access layer is built around a generic `Repository<T, ID>` interface to allow swap-in mock storage engines or SQL databases.

---

## 🚀 Setup & Execution Instructions

### Prerequisites
* **Java JDK**: Version 17 or higher (fully tested and compiled under Java 24)
* **Maven**: Version 3.6 or higher (optional, standard compiler fallback supported)

### 📦 Build and Compile
From the project root directory, compile all source code:
```bash
# Using Maven:
mvn clean compile

# Or compile manually using standard Java Compiler:
javac -d target/classes src/main/java/com/library/*.java src/main/java/com/library/*/*.java
```

### 🎮 Run the Application
Start the interactive Console CLI loop:
```bash
# Using Maven:
mvn exec:java -Dexec.mainClass="com.library.Main"

# Or run target class directly:
java -cp target/classes com.library.Main
```

### 🧪 Running the Test Suite
The application contains a robust suite of JUnit 5 tests verifying core logic, business constraints, validation rules, active loan guards, and encapsulation integrity:
```bash
# Execute Maven surefire plugin tests:
mvn test
```

---

## 📁 Source Code Directory Structure
```text
Library_management_System/
│
├── src/
│   ├── main/java/com/library/
│   │   ├── Main.java              # Entry point connecting MVC components
│   │   ├── controller/
│   │   │   └── LibraryController.java # Interprets inputs & coordinates actions
│   │   ├── model/
│   │   │   ├── LibraryItem.java   # Abstract parent model class
│   │   │   ├── Book.java          # Multi-copy book domain model
│   │   │   ├── Member.java        # Encapsulated member domain model
│   │   │   └── Loan.java          # Loan transactional record model
│   │   ├── repository/
│   │   │   ├── Repository.java    # Generic storage interface
│   │   │   ├── BookRepository.java
│   │   │   ├── MemberRepository.java
│   │   │   └── LoanRepository.java
│   │   ├── service/
│   │   │   └── LibraryService.java# Orchestrates core business and validation rules
│   │   ├── util/
│   │   │   └── CSVHandler.java    # CSV reader/writer with backward compatibility
│   │   └── view/
│   │       └── ConsoleView.java   # Renders menus and prompts in the console
│   │
│   └── test/java/com/library/service/
│       └── LibraryServiceTest.java# JUnit 5 test suite
│
├── pom.xml                        # Maven dependency and build config
└── README.md                      # Documentation
```
