package com.library.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic Repository interface.
 * An interface acts as a contract. It forces any class that uses it to include these methods.
 * <T> stands for the Type (like Book or Member), and <ID> stands for the ID type (like String).
 */
public interface Repository<T, ID> {
    void add(T item); // Save a new item
    void update(T item); // Update an existing item
    void remove(ID id); // Delete an item by its ID
    Optional<T> findById(ID id); // Look for an item by its ID (Optional means it might not exist)
    List<T> findAll(); // Get a list of all items
}
