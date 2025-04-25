package org.example.giaodienthuvien;

public class Document {
    private String title;
    private String author;
    private int quantity;
    private String borrowedBy; // Thêm trường lưu thông tin người mượn

    public Document(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.borrowedBy = null; // Khi tài liệu chưa được mượn, borrowedBy sẽ là null
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
        this.quantity = quantity;
    }

    // Getter và setter cho borrowedBy
    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    // Kiểm tra xem tài liệu có sẵn để mượn không
    public boolean isAvailable() {
        return borrowedBy == null; // Tài liệu có sẵn nếu không có người mượn
    }
}
