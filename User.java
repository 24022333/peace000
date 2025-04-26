package com.example.giaodien;

public class User {
    private String name;
    private String memberId;
    private int borrowedCount;

    public User(String name, String memberId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty");
        }
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void borrowDocument() {
        borrowedCount++;
    }

    public void returnDocument() {
        if (borrowedCount > 0) {
            borrowedCount--;
        } else {
            throw new IllegalStateException("No documents borrowed by this user");
        }
    }
}
