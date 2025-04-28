package org.example.thuvien;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private String title;
    private String author;
    private int quantity;
    private List<String> borrowedBy;

    public Document(String title, String author, int quantity) {
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Title and author cannot be empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.borrowedBy = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public List<String> getBorrowedBy() {
        return borrowedBy;
    }

    public void addBorrower(String memberId) {
        borrowedBy.add(memberId);
    }

    public void removeBorrower(String memberId) {
        borrowedBy.remove(memberId);
    }

    public boolean isAvailable() {
        return quantity > borrowedBy.size();
    }

    // Thêm phương thức tính số lượng khả dụng
    public int getAvailableQuantity() {
        return quantity - borrowedBy.size();
    }
}
