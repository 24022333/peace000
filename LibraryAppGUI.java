package org.example.giaodienthuvien;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class LibraryAppGUI extends Application {
    private Library library = new Library();
    private ObservableList<Document> documentList = FXCollections.observableArrayList(library.getDocuments());

    @Override
    public void start(Stage primaryStage) {
        // Tạo các nút chức năng
        Button addDocumentButton = new Button("Add Document");
        Button removeDocumentButton = new Button("Remove Document");
        Button updateDocumentButton = new Button("Update Document");
        Button borrowDocumentButton = new Button("Borrow Document");
        Button returnDocumentButton = new Button("Return Document");

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

        // Hành động cho các nút
        addDocumentButton.setOnAction(e -> addDocument());
        removeDocumentButton.setOnAction(e -> removeDocument());
        updateDocumentButton.setOnAction(e -> updateDocument());
        borrowDocumentButton.setOnAction(e -> borrowDocument());
        returnDocumentButton.setOnAction(e -> returnDocument());

        // Style cho các nút
        for (Button btn : new Button[]{addDocumentButton, removeDocumentButton, updateDocumentButton, borrowDocumentButton, returnDocumentButton}) {
            btn.setPrefWidth(140);
        }

        // Bố cục các nút căn giữa
        HBox buttonLayout = new HBox(10, addDocumentButton, removeDocumentButton, updateDocumentButton, borrowDocumentButton, returnDocumentButton);
        buttonLayout.setAlignment(Pos.CENTER);  // Căn giữa các nút
        buttonLayout.setStyle("-fx-padding: 10;");

        // Bố cục chính
        VBox layout = new VBox(20, buttonLayout, documentTable);
        layout.setStyle("-fx-padding: 20px;");

        // Tạo scene
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System - Dark Mode");
        primaryStage.show();
    }

    // Các phương thức xử lý
    private void addDocument() {
        // Tạo tài liệu mới
        Document doc = new Document("Java Programming", "John Doe", 5);

        // Thêm tài liệu vào thư viện
        library.addDocument(doc);

        // Cập nhật lại danh sách tài liệu trong ObservableList
        documentList.setAll(library.getDocuments());

        System.out.println("Document added.");
    }

    private void removeDocument() {
        Document doc = library.findDocument("Java Programming");
        if (doc != null) {
            library.removeDocument(doc.getTitle());
            documentList.setAll(library.getDocuments());
            System.out.println("Document removed.");
        }
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
        library.borrowDocument("Java Programming", "123");
        documentList.setAll(library.getDocuments());
    }

    private void returnDocument() {
        library.returnDocument("Java Programming", "123");
        documentList.setAll(library.getDocuments());
    }

    public static void main(String[] args) {
        launch(args);
    }
}