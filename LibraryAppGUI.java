package com.example.giaodien;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private List<Document> documents = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public void addDocument(Document doc) {
        if (findDocument(doc.getTitle()) != null) {
            throw new IllegalArgumentException("Document with title '" + doc.getTitle() + "' already exists");
        }
        documents.add(doc);
    }

    public void removeDocument(String title) {
        Document doc = findDocument(title);
        if (doc == null) {
            throw new IllegalArgumentException("Document not found");
        }
        if (!doc.getBorrowedBy().isEmpty()) {
            throw new IllegalStateException("Cannot remove document while it is borrowed");
        }
        documents.remove(doc);
    }

    public Document findDocument(String title) {
        return documents.stream()
                .filter(doc -> doc.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        if (findUser(user.getMemberId()) != null) {
            throw new IllegalArgumentException("User with ID '" + user.getMemberId() + "' already exists");
        }
        users.add(user);
    }

    public User findUser(String memberId) {
        return users.stream()
                .filter(user -> user.getMemberId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    public void borrowDocument(String title, String memberId) {
        Document doc = findDocument(title);
        User user = findUser(memberId);
        if (doc == null) {
            throw new IllegalArgumentException("Document not found");
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!doc.isAvailable()) {
            throw new IllegalStateException("Document is not available");
        }
        doc.addBorrower(memberId);
        user.borrowDocument();
    }

    public void returnDocument(String title, String memberId) {
        Document doc = findDocument(title);
        User user = findUser(memberId);
        if (doc == null) {
            throw new IllegalArgumentException("Document not found");
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!doc.getBorrowedBy().contains(memberId)) {
            throw new IllegalStateException("Document was not borrowed by this user");
        }
        doc.removeBorrower(memberId);
        user.returnDocument();
    }

    public List<Document> getDocuments() {
        return new ArrayList<>(documents);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
