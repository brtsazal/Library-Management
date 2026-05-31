package com.library.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.model.Book;

/**
 * Stores and manages all the books in memory (RAM).
 * Implements the Repository interface for Book objects.
 */
public class BookRepository implements Repository<Book, String> {
    private List<Book> books; // The list that acts as our database for books

    public BookRepository() {
        this.books = new ArrayList<>(); // Initialize as an empty list
    }

    @Override
    public void add(Book item) {
        books.add(item); // Add the book to our list
    }

    @Override
    public void update(Book item) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(item.getId())) {
                books.set(i, item);
                return;
            }
        }
    }

    @Override
    public void remove(String id) {
        // Look through the list and remove the book if the ID matches
        books.removeIf(book -> book.getId().equals(id));
    }

    @Override
    public Optional<Book> findById(String id) {
        // Look for the book. Returns Optional to safely handle cases where the book is not found
        return books.stream().filter(book -> book.getId().equals(id)).findFirst();
    }

    @Override
    public List<Book> findAll() {
        // Return a copy of the list to prevent direct outside modifications
        return new ArrayList<>(books);
    }
}
