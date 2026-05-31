package com.library.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.model.Member;

/**
 * Stores and manages all the library members in memory.
 */
public class MemberRepository implements Repository<Member, String> {
    private List<Member> members; // The list that acts as our database for members

    public MemberRepository() {
        this.members = new ArrayList<>();
    }

    @Override
    public void add(Member item) {
        members.add(item); // Add member to the list
    }

    @Override
    public void update(Member item) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId().equals(item.getMemberId())) {
                members.set(i, item);
                return;
            }
        }
    }

    @Override
    public void remove(String id) {
        // Remove member if ID matches
        members.removeIf(member -> member.getMemberId().equals(id));
    }

    @Override
    public Optional<Member> findById(String id) {
        // Find and return member if they exist
        return members.stream().filter(member -> member.getMemberId().equals(id)).findFirst();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(members); // Return a copy of all members
    }
}
