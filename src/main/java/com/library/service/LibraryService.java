package com.library.service;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LibraryService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public LibraryService(BookRepository bookRepository, MemberRepository memberRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    public void initialize() {
    }

    public void addBook(String title, String author, String isbn, int copies) {
        boolean exists = bookRepository.findAll().stream()
                .anyMatch(book -> book.getIsbn().equals(isbn));
        if (exists) {
            throw new IllegalArgumentException("A book with this ISBN already exists.");
        }

        String id = generateNextBookId();
        bookRepository.add(new Book(id, title, author, isbn, copies));
    }

    public void addBook(String title, String author, String isbn) {
        addBook(title, author, isbn, 1);
    }

    public void updateBook(String id, String title, String author, String isbn) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found.");
        }

        Book book = bookOpt.get();
        if (!isbn.isEmpty() && !isbn.equals(book.getIsbn())) {
            boolean exists = bookRepository.findAll().stream()
                    .anyMatch(existing -> existing.getIsbn().equals(isbn));
            if (exists) {
                throw new IllegalArgumentException("A book with this ISBN already exists.");
            }
            book.setIsbn(isbn);
        }
        if (!title.isEmpty()) {
            book.setTitle(title);
        }
        if (!author.isEmpty()) {
            book.setAuthor(author);
        }
        bookRepository.update(book);
    }

    public void removeBook(String id) {
        Book book = getBook(id);
        if (book.getAvailableCopies() < book.getCopies()) {
            throw new IllegalStateException("Cannot remove a book that has active loans.");
        }
        bookRepository.remove(id);
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.search(query);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
    }

    public void registerMember(String name, String email) {
        validateEmail(email);
        String id = generateNextMemberId();
        memberRepository.add(new Member(id, name, email));
    }

    public void updateMember(String id, String name, String email) {
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Member not found.");
        }

        Member member = memberOpt.get();
        if (!email.isEmpty()) {
            validateEmail(email);
            member.setEmail(email);
        }
        if (!name.isEmpty()) {
            member.setName(name);
        }
        memberRepository.update(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMember(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));
    }

    public void borrowBook(String memberId, String bookId) {
        Member member = getMember(memberId);
        Book book = getBook(bookId);

        if (member.getCurrentLoans().size() >= 3) {
            throw new IllegalStateException("Member has already borrowed the maximum limit of 3 books.");
        }

        if (book.getAvailableCopies() == 0) {
            book.addToWaitlist(member);
            throw new IllegalStateException("Book is already borrowed. You have been added to the waitlist.");
        }

        String loanId = generateNextLoanId();
        Loan loan = new Loan(loanId, book, member, LocalDate.now(), LocalDate.now().plusDays(14));

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        member.addLoan(loan);
        loanRepository.add(loan);
    }

    public String returnBook(String memberId, String bookId) {
        Member member = getMember(memberId);
        Book book = getBook(bookId);

        Optional<Loan> activeLoanOpt = member.getCurrentLoans().stream()
                .filter(loan -> loan.getBook().getId().equals(bookId) && !loan.isReturned())
                .findFirst();

        if (activeLoanOpt.isEmpty()) {
            throw new IllegalArgumentException("No active loan found for this book and member.");
        }

        Loan loan = activeLoanOpt.get();
        loan.setReturnDate(LocalDate.now());
        member.removeLoan(loan);
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        String message = "Book returned successfully.";
        if (!book.getWaitlist().isEmpty()) {
            Member nextInLine = book.getWaitlist().get(0);
            book.removeFromWaitlist(nextInLine);
            message += "\nNotice: Book '" + book.getTitle() + "' is now available for waitlisted member: "
                    + nextInLine.getName() + " (ID: " + nextInLine.getMemberId() + ")";
        }

        return message;
    }

    private void validateEmail(String email) {
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private String generateNextBookId() {
        int maxId = 0;
        for (Book book : bookRepository.findAll()) {
            try {
                if (book.getId().startsWith("B")) {
                    int idNum = Integer.parseInt(book.getId().substring(1));
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.format("B%03d", maxId + 1);
    }

    private String generateNextMemberId() {
        int maxId = 0;
        for (Member member : memberRepository.findAll()) {
            try {
                if (member.getMemberId().startsWith("M")) {
                    int idNum = Integer.parseInt(member.getMemberId().substring(1));
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.format("M%03d", maxId + 1);
    }

    private String generateNextLoanId() {
        int maxId = 0;
        for (Loan loan : loanRepository.findAll()) {
            try {
                if (loan.getLoanId().startsWith("L")) {
                    int idNum = Integer.parseInt(loan.getLoanId().substring(1));
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.format("L%03d", maxId + 1);
    }
}
