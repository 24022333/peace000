package org.example.giaodienthuvien;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Document> documents = new ArrayList<>();

    // Thêm tài liệu
    public void addDocument(Document doc) {
        documents.add(doc);
    }

    // Xóa tài liệu theo tên
    public void removeDocument(String title) {
        documents.removeIf(doc -> doc.getTitle().equals(title));
    }

    // Tìm tài liệu theo tên
    public Document findDocument(String title) {
        return documents.stream()
                .filter(doc -> doc.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    // Mượn tài liệu
    public void borrowDocument(String title, String userId) {
        Document doc = findDocument(title);
        if (doc != null && doc.getQuantity() > 0) {
            doc.setQuantity(doc.getQuantity() - 1);
            System.out.println("Document borrowed by user: " + userId);
        } else {
            System.out.println("Document is not available or not found.");
        }
    }

    // Trả tài liệu
    public void returnDocument(String title, String userId) {
        Document doc = findDocument(title);
        if (doc != null) {
            doc.setQuantity(doc.getQuantity() + 1);
            System.out.println("Document returned by user: " + userId);
        }
    }

    // Phương thức trả về danh sách tài liệu
    public List<Document> getDocuments() {
        return documents;
    }
}