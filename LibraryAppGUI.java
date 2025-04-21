package org.example.giaodienthuvien;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.net.URL;

public class LibraryAppGUI extends Application {
    private Library library = new Library();
    private ObservableList<Document> documentList = FXCollections.observableArrayList(library.getDocuments());
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Tạo các nút chức năng
        Button addDocumentButton = new Button("Add Document");
        Button removeDocumentButton = new Button("Remove Document");
        Button updateDocumentButton = new Button("Update Document");
        Button borrowDocumentButton = new Button("Borrow Document");
        Button returnDocumentButton = new Button("Return Document");

        Button findDocumentButton = new Button("Find Document");
        Button displayDocumentButton = new Button("Display Document");
        Button addUserButton = new Button("Add User");
        Button displayUserButton = new Button("Display User Info");
        Button exitButton = new Button("Exit");

        // Tạo bảng hiển thị tài liệu
        TableView<Document> documentTable = new TableView<>(documentList);
        documentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Document, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Document, String> authorColumn = new TableColumn<>("Author");
        TableColumn<Document, Integer> quantityColumn = new TableColumn<>("Quantity");

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        titleColumn.setStyle("-fx-alignment: CENTER;");
        authorColumn.setStyle("-fx-alignment: CENTER;");
        quantityColumn.setStyle("-fx-alignment: CENTER;");

        documentTable.getColumns().addAll(titleColumn, authorColumn, quantityColumn);

        // Style cho các nút
        for (Button btn : new Button[] {
                addDocumentButton, removeDocumentButton, updateDocumentButton,
                borrowDocumentButton, returnDocumentButton, findDocumentButton,
                displayDocumentButton, addUserButton, displayUserButton, exitButton}) {
            btn.setPrefWidth(140);
        }

        // Bố cục các nút
        HBox buttonLayout = new HBox(10, addDocumentButton, removeDocumentButton, updateDocumentButton, borrowDocumentButton, returnDocumentButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setStyle("-fx-padding: 10;");

        HBox extraButtons = new HBox(10, findDocumentButton, displayDocumentButton, addUserButton, displayUserButton, exitButton);
        extraButtons.setAlignment(Pos.CENTER);
        extraButtons.setStyle("-fx-padding: 10;");

        // Bố cục chính
        VBox contentLayout = new VBox(20, buttonLayout, extraButtons, documentTable);
        contentLayout.setStyle("-fx-padding: 20px;");

        // Ảnh nền
        StackPane root = new StackPane();
        ImageView bgView = null;
        URL imageUrl = getClass().getResource("/images/background.jpg");
        if (imageUrl == null) {
            System.out.println("Ảnh nền không tìm thấy!");
        } else {
            Image bgImage = new Image(imageUrl.toExternalForm());
            bgView = new ImageView(bgImage);
            bgView.fitWidthProperty().bind(root.widthProperty());
            bgView.fitHeightProperty().bind(root.heightProperty());
        }

        // StackPane để chồng nền và nội dung
        if (bgView != null) {
            root.getChildren().addAll(bgView, contentLayout);
        } else {
            root.getChildren().add(contentLayout);
        }

        // Tạo scene
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System - Dark Mode");
        primaryStage.show();

        // Hành động các nút
        addDocumentButton.setOnAction(e -> addDocument());
        removeDocumentButton.setOnAction(e -> removeDocument());
        updateDocumentButton.setOnAction(e -> updateDocument());
        borrowDocumentButton.setOnAction(e -> borrowDocument());
        returnDocumentButton.setOnAction(e -> returnDocument());

        findDocumentButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Find Document");
            dialog.setHeaderText("Enter document title:");
            dialog.setContentText("Title:");
            dialog.showAndWait().ifPresent(title -> {
                Document doc = library.findDocument(title);
                if (doc != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Document Found");
                    alert.setHeaderText("Document Info:");
                    alert.setContentText("Title: " + doc.getTitle() + "\nAuthor: " + doc.getAuthor() + "\nQuantity: " + doc.getQuantity());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("Document not found.");
                    alert.showAndWait();
                }
            });
        });

        // Đã sửa phần này
        displayDocumentButton.setOnAction(e -> {
            documentList.setAll(library.getDocuments());
        });

        addUserButton.setOnAction(e -> {
            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("Add User");

            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField();
            Label idLabel = new Label("Member ID:");
            TextField idField = new TextField();

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.add(nameLabel, 0, 0); grid.add(nameField, 1, 0);
            grid.add(idLabel, 0, 1); grid.add(idField, 1, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(button -> {
                if (button == ButtonType.OK) {
                    return new User(nameField.getText(), idField.getText());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(user -> {
                userList.add(user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Added");
                alert.setContentText("User " + user.getName() + " added!");
                alert.showAndWait();
            });
        });

        displayUserButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            if (userList.isEmpty()) {
                sb.append("No users available.");
            } else {
                for (User u : userList) {
                    sb.append("Name: ").append(u.getName())
                            .append(" | ID: ").append(u.getMemberId())
                            .append(" | Borrowed: ").append(u.getBorrowedCount()).append("\n");
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User List");
            alert.setHeaderText("Registered Users:");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        });

        exitButton.setOnAction(e -> primaryStage.close());
    }

    private void addDocument() {
        Dialog<Document> dialog = new Dialog<>();
        dialog.setTitle("Add Document");

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();
        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(titleLabel, 0, 0); grid.add(titleField, 1, 0);
        grid.add(authorLabel, 0, 1); grid.add(authorField, 1, 1);
        grid.add(quantityLabel, 0, 2); grid.add(quantityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    return new Document(titleField.getText(), authorField.getText(), quantity);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setContentText("Quantity must be a number.");
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(doc -> {
            library.addDocument(doc);
            documentList.setAll(library.getDocuments());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Document Added");
            alert.setContentText("Document \"" + doc.getTitle() + "\" added successfully!");
            alert.showAndWait();
        });
    }

    private void removeDocument() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Document");
        dialog.setHeaderText("Enter document title to remove:");
        dialog.setContentText("Title:");

        dialog.showAndWait().ifPresent(title -> {
            Document doc = library.findDocument(title);
            if (doc != null) {
                library.removeDocument(doc.getTitle());
                documentList.setAll(library.getDocuments());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Document removed successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "Document with the title \"" + title + "\" not found.");
            }
        });
    }

    private void updateDocument() {
        Document doc = library.findDocument("Java Programming");
        if (doc != null) {
            doc.setQuantity(10);
            documentList.setAll(library.getDocuments());
            System.out.println("Document updated.");
        } else {
            System.out.println("Document not found.");
        }
    }

    private void borrowDocument() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Borrow Document");

        Label titleLabel = new Label("Document Title:");
        TextField titleField = new TextField();
        Label memberLabel = new Label("Member ID:");
        TextField memberField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(memberLabel, 0, 1);
        grid.add(memberField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(titleField.getText(), memberField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String title = result.getKey();
            String memberId = result.getValue();
            Document doc = library.findDocument(title);

            if (doc == null) {
                showAlert(Alert.AlertType.WARNING, "Not Found", "Document not found.");
            } else if (!doc.isAvailable()) {
                showAlert(Alert.AlertType.WARNING, "Unavailable", "Document is currently borrowed.");
            } else {
                doc.setBorrowedBy(memberId);
                documentList.setAll(library.getDocuments());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Document borrowed successfully!");
            }
        });
    }

    private void returnDocument() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Return Document");
        dialog.setHeaderText("Enter Member ID:");
        dialog.setContentText("Member ID:");

        dialog.showAndWait().ifPresent(memberId -> {
            Document doc = library.findDocument("Java Programming");
            if (doc != null && memberId.equals(doc.getBorrowedBy())) {
                doc.setBorrowedBy(null);  // Reset borrowedBy when returning the document
                documentList.setAll(library.getDocuments());
                System.out.println("Document returned by " + memberId);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Return");
                alert.setHeaderText("You cannot return this document.");
                alert.setContentText("This document is not borrowed by you.");
                alert.showAndWait();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
