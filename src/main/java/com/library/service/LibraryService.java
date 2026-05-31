package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class LibraryService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LibraryService(BookRepository bookRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
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
        getBook(id);
        bookRepository.remove(id);
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
}
