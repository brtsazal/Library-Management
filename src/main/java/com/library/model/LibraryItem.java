package com.library.model;

/**
 * Represents a generic library item with basic information.
 */
public class LibraryItem {
    private String id; // Unique ID for the item
    private String title; // Title of the item

    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
