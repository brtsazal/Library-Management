package com.library.model;

/**
 * Represents a library user with basic contact information.
 */
public class Member {
    private String memberId; // Unique ID for the member
    private String name; // Member's full name
    private String email; // Member's email address

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member [ID=" + memberId + ", Name=" + name + ", Email=" + email + "]";
    }
}
