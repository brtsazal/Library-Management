package com.library.model;

/**
 * Abstract parent for library items that share the same core identity.
 */
public abstract class LibraryItem {
    protected String id; // Unique ID for the item
    protected String title; // Title of the item

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

    public abstract String getType();
}
