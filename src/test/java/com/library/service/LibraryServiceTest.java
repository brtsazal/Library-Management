package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryServiceTest {
    private LibraryService service;

    @BeforeEach
    void setUp() {
        BookRepository bookRepo = new BookRepository();
        MemberRepository memberRepo = new MemberRepository();
        LoanRepository loanRepo = new LoanRepository();
        service = new LibraryService(bookRepo, memberRepo, loanRepo);
    }

    @Test
    void testAddAndSearchBook() {
        service.addBook("The Hobbit", "J.R.R. Tolkien", "1111");
        List<Book> results = service.searchBooks("Hobbit");
        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).getTitle());
    }

    @Test
    void testBorrowBookSuccess() {
        service.addBook("1984", "George Orwell", "1234");
        service.registerMember("Alice", "alice@example.com");

        Book book = service.searchBooks("1984").get(0);
        Member member = service.getAllMembers().get(0);

        assertDoesNotThrow(() -> service.borrowBook(member.getMemberId(), book.getId()));
        assertTrue(book.isBorrowed());
        assertEquals(1, member.getCurrentLoans().size());
    }

    @Test
    void testBorrowBookLimitExceeded() {
        service.addBook("Book 1", "Author", "1");
        service.addBook("Book 2", "Author", "2");
        service.addBook("Book 3", "Author", "3");
        service.addBook("Book 4", "Author", "4");
        service.registerMember("Bob", "bob@example.com");

        List<Book> books = service.getAllBooks();
        Member member = service.getAllMembers().get(0);

        service.borrowBook(member.getMemberId(), books.get(0).getId());
        service.borrowBook(member.getMemberId(), books.get(1).getId());
        service.borrowBook(member.getMemberId(), books.get(2).getId());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.borrowBook(member.getMemberId(), books.get(3).getId()));

        assertEquals("Member has already borrowed the maximum limit of 3 books.", exception.getMessage());
    }

    @Test
    void testWaitlistFeature() {
        service.addBook("Dune", "Frank Herbert", "999");
        service.registerMember("Charlie", "charlie@example.com");
        service.registerMember("Dave", "dave@example.com");

        Book book = service.searchBooks("Dune").get(0);
        Member charlie = service.getAllMembers().get(0);
        Member dave = service.getAllMembers().get(1);

        service.borrowBook(charlie.getMemberId(), book.getId());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.borrowBook(dave.getMemberId(), book.getId()));

        assertTrue(exception.getMessage().contains("waitlist"));
        assertEquals(1, book.getWaitlist().size());
        assertEquals(dave, book.getWaitlist().get(0));

        String returnMsg = service.returnBook(charlie.getMemberId(), book.getId());
        assertTrue(returnMsg.contains("available for waitlisted member: Dave"));
        assertEquals(0, book.getWaitlist().size());
    }

    @Test
    void testDuplicateIsbnCheck() {
        service.addBook("Book A", "Author A", "ISBN-111");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.addBook("Book B", "Author B", "ISBN-111"));
        assertEquals("A book with this ISBN already exists.", exception.getMessage());
    }

    @Test
    void testEmailFormatValidation() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> service.registerMember("Alice", "invalid-email"));
        assertEquals("Invalid email format.", exception1.getMessage());

        service.registerMember("Bob", "bob@example.com");
        Member member = service.getAllMembers().get(0);

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> service.updateMember(member.getMemberId(), "Bob Updated", "bademail"));
        assertEquals("Invalid email format.", exception2.getMessage());
    }

    @Test
    void testActiveLoanBookRemovalGuard() {
        service.addBook("To Kill a Mockingbird", "Harper Lee", "ISBN-222", 1);
        service.registerMember("Member A", "membera@example.com");

        Book book = service.searchBooks("Mockingbird").get(0);
        Member member = service.getAllMembers().get(0);

        service.borrowBook(member.getMemberId(), book.getId());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.removeBook(book.getId()));
        assertEquals("Cannot remove a book that has active loans.", exception.getMessage());
    }

    @Test
    void testMultipleCopiesSupport() {
        service.addBook("Modern Java", "Author", "ISBN-333", 2);
        service.registerMember("User 1", "u1@example.com");
        service.registerMember("User 2", "u2@example.com");
        service.registerMember("User 3", "u3@example.com");

        Book book = service.searchBooks("Modern Java").get(0);
        List<Member> members = service.getAllMembers();
        Member u1 = members.get(0);
        Member u2 = members.get(1);
        Member u3 = members.get(2);

        assertDoesNotThrow(() -> service.borrowBook(u1.getMemberId(), book.getId()));
        assertEquals(1, book.getAvailableCopies());
        assertFalse(book.isBorrowed());

        assertDoesNotThrow(() -> service.borrowBook(u2.getMemberId(), book.getId()));
        assertEquals(0, book.getAvailableCopies());
        assertTrue(book.isBorrowed());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.borrowBook(u3.getMemberId(), book.getId()));
        assertTrue(exception.getMessage().contains("waitlist"));
        assertEquals(1, book.getWaitlist().size());
        assertEquals(u3, book.getWaitlist().get(0));

        String returnMsg = service.returnBook(u1.getMemberId(), book.getId());
        assertTrue(returnMsg.contains("available for waitlisted member: User 3"));
        assertEquals(1, book.getAvailableCopies());
        assertEquals(0, book.getWaitlist().size());
    }

    @Test
    void testEncapsulationIsEnforced() {
        service.registerMember("Alice", "alice@example.com");
        Member member = service.getAllMembers().get(0);

        assertThrows(UnsupportedOperationException.class, () -> member.getBorrowingHistory().add(null));
        assertThrows(UnsupportedOperationException.class, () -> member.getCurrentLoans().add(null));
    }
}
