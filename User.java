package org.example.giaodienthuvien;

public class User {
    private String name;
    private String memberId;
    private int borrowedCount;

    public User(String name, String memberId) {
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
        borrowedCount--;
    }
}
