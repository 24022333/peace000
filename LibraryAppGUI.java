package org.example.thuvien;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.io.InputStream;

public class LibraryAppGUI extends Application {
    private Library library = new Library();
    private ObservableList<Document> documentList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Khởi tạo dữ liệu (không có dữ liệu mẫu)
        initializeSampleData();

        // Tạo bố cục chính
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Tiêu đề
        Label titleLabel = new Label("Hệ Thống Quản Lý Thư Viện");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));
        titleBox.setStyle("-fx-background-color: #1C1C1C");
        root.setTop(titleBox);

        // Bảng tài liệu
        TableView<Document> documentTable = new TableView<>();
        documentTable.setItems(documentList);
        documentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        documentTable.setPlaceholder(new Label("Chưa có tài liệu nào. Nhấn 'Thêm Tài Liệu' để bắt đầu."));

        TableColumn<Document, String> titleColumn = new TableColumn<>("Tiêu đề");
        TableColumn<Document, String> authorColumn = new TableColumn<>("Tác giả");
        TableColumn<Document, Integer> availableQuantityColumn = new TableColumn<>("Số lượng khả dụng");

        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        availableQuantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableQuantity()).asObject());

        documentTable.getColumns().addAll(titleColumn, authorColumn, availableQuantityColumn);
        documentTable.setStyle("-fx-background-color: #2F2F2F;");

        // Hiển thị thông tin chi tiết khi chọn tài liệu
        documentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDocumentDetails(newSelection);
            }
        });

        // Các nút chức năng
        Button addDocumentButton = createStyledButton("Thêm Tài Liệu");
        Button removeDocumentButton = createStyledButton("Xóa Tài Liệu");
        Button updateDocumentButton = createStyledButton("Cập Nhật Tài Liệu");
        Button borrowDocumentButton = createStyledButton("Mượn Tài Liệu");
        Button returnDocumentButton = createStyledButton("Trả Tài Liệu");
        Button findDocumentButton = createStyledButton("Tìm Tài Liệu");
        Button displayDocumentButton = createStyledButton("Hiển Thị Tài Liệu");
        Button addUserButton = createStyledButton("Thêm Người Dùng");
        Button displayUserButton = createStyledButton("Hiển Thị Người Dùng");
        Button exitButton = createStyledButton("Thoát");


        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.add(addDocumentButton, 0, 0);
        buttonGrid.add(removeDocumentButton, 1, 0);
        buttonGrid.add(updateDocumentButton, 2, 0);
        buttonGrid.add(borrowDocumentButton, 3, 0);
        buttonGrid.add(returnDocumentButton, 4, 0);

        buttonGrid.add(findDocumentButton, 0, 1);
        buttonGrid.add(displayDocumentButton, 1, 1);
        buttonGrid.add(addUserButton, 2, 1);
        buttonGrid.add(displayUserButton, 3, 1);
        buttonGrid.add(exitButton, 4, 1);

        VBox centerLayout = new VBox(10, documentTable, buttonGrid);
        centerLayout.setPadding(new Insets(10));
        root.setCenter(centerLayout);

        // Ảnh nền
        InputStream bgStream = getClass().getResourceAsStream("/images/background.jpg");
        if (bgStream != null) {
            Image bgImage = new Image(bgStream);
            BackgroundImage backgroundImage = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            root.setBackground(new Background(backgroundImage));
        } else {
            root.setStyle("-fx-background-color: #34495e;");
        }

        // Tạo Scene
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css") != null ?
                getClass().getResource("/style.css").toExternalForm() : "");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hệ Thống Quản Lý Thư Viện");
        primaryStage.show();

        // Xử lý sự kiện nút
        addDocumentButton.setOnAction(e -> addDocument());
        removeDocumentButton.setOnAction(e -> removeDocument());
        updateDocumentButton.setOnAction(e -> updateDocument());
        borrowDocumentButton.setOnAction(e -> borrowDocument());
        returnDocumentButton.setOnAction(e -> returnDocument());
        findDocumentButton.setOnAction(e -> findDocument());
        displayDocumentButton.setOnAction(e -> displayDocuments());
        addUserButton.setOnAction(e -> addUser());
        displayUserButton.setOnAction(e -> displayUsers());
        exitButton.setOnAction(e -> primaryStage.close());
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498db; "
                + "-fx-text-fill: white; "
                + "-fx-font-size: 14px; "
                + "-fx-padding: 10px; "
                + "-fx-pref-width: 150px; "
                + "-fx-border-color: #1F9C99; "
                + "-fx-border-width: 2px; "
                + "-fx-border-radius: 12px; "
                + "-fx-background-radius: 12px; "
                + "-fx-effect: dropshadow(gaussian, #1F9C99, 10, 0, 0, 5);"); // Chỉ dùng dropshadow, bỏ glow

        // Thêm hiệu ứng phóng to khi nhấn nút
        button.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.2);  // Phóng to theo trục X
            scale.setToY(1.2);  // Phóng to theo trục Y
            scale.play();
        });

        // Thêm hiệu ứng trở lại kích thước bình thường khi thả nút
        button.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1);  // Trở lại kích thước bình thường
            scale.setToY(1);  // Trở lại kích thước bình thường
            scale.play();
        });

        return button;
    }

    private void initializeSampleData() {
        try {
            // Không khởi tạo dữ liệu mẫu (bỏ cả tài liệu và người dùng)
            documentList.setAll(library.getDocuments());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi khởi tạo", e.getMessage());
        }
    }

    private void addDocument() {
        Dialog<Document> dialog = new Dialog<>();
        dialog.setTitle("Thêm Tài Liệu");

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField quantityField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Tiêu đề:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Tác giả:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Số lượng:"), 0, 2);
        grid.add(quantityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    return new Document(titleField.getText(), authorField.getText(), quantity);
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi đầu vào", "Vui lòng nhập số lượng hợp lệ và điền đầy đủ thông tin.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(doc -> {
            try {
                library.addDocument(doc);
                documentList.setAll(library.getDocuments());
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm tài liệu: " + doc.getTitle());
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        });
    }

    private void removeDocument() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Xóa Tài Liệu");
        dialog.setHeaderText("Nhập tiêu đề tài liệu cần xóa:");
        dialog.setContentText("Tiêu đề:");

        dialog.showAndWait().ifPresent(title -> {
            try {
                library.removeDocument(title);
                documentList.setAll(library.getDocuments());
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa tài liệu.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        });
    }

    private void updateDocument() {
        Dialog<Document> dialog = new Dialog<>();
        dialog.setTitle("Cập Nhật Tài Liệu");

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField quantityField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Tiêu đề:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Tác giả:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Số lượng:"), 0, 2);
        grid.add(quantityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    return new Document(titleField.getText(), authorField.getText(), quantity);
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi đầu vào", "Vui lòng nhập số lượng hợp lệ và điền đầy đủ thông tin.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(doc -> {
            try {
                Document existingDoc = library.findDocument(doc.getTitle());
                if (existingDoc != null) {
                    library.removeDocument(doc.getTitle());
                    library.addDocument(doc);
                    documentList.setAll(library.getDocuments());
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã cập nhật tài liệu: " + doc.getTitle());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Tài liệu không tồn tại.");
                }
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        });
    }

    private void borrowDocument() {
        // Kiểm tra xem có người dùng nào không trước khi cho mượn
        if (library.getUsers().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Không có người dùng", "Vui lòng thêm người dùng trước khi mượn tài liệu.");
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Mượn Tài Liệu");

        TextField titleField = new TextField();
        TextField memberIdField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Tiêu đề tài liệu:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Mã thành viên:"), 0, 1);
        grid.add(memberIdField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Pair<>(titleField.getText(), memberIdField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            try {
                library.borrowDocument(pair.getKey(), pair.getValue());
                documentList.setAll(library.getDocuments());
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã mượn tài liệu.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        });
    }

    private void returnDocument() {
        // Kiểm tra xem có người dùng nào không trước khi trả
        if (library.getUsers().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Không có người dùng", "Vui lòng thêm người dùng trước khi trả tài liệu.");
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Trả Tài Liệu");

        TextField titleField = new TextField();
        TextField memberIdField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Tiêu đề tài liệu:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Mã thành viên:"), 0, 1);
        grid.add(memberIdField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Pair<>(titleField.getText(), memberIdField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            try {
                library.returnDocument(pair.getKey(), pair.getValue());
                documentList.setAll(library.getDocuments());
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã trả tài liệu.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        });
    }

    private void findDocument() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tìm Tài Liệu");
        dialog.setHeaderText("Nhập tiêu đề tài liệu:");
        dialog.setContentText("Tiêu đề:");

        dialog.showAndWait().ifPresent(title -> {
            Document doc = library.findDocument(title);
            if (doc != null) {
                showAlert(Alert.AlertType.INFORMATION, "Tìm thấy tài liệu",
                        "Tiêu đề: " + doc.getTitle() + "\nTác giả: " + doc.getAuthor() +
                                "\nSố lượng khả dụng: " + doc.getAvailableQuantity());
            } else {
                showAlert(Alert.AlertType.WARNING, "Không tìm thấy", "Tài liệu không tồn tại.");
            }
        });
    }

    private void displayDocuments() {
        StringBuilder sb = new StringBuilder();
        if (library.getDocuments().isEmpty()) {
            sb.append("Chưa có tài liệu nào. Nhấn 'Thêm Tài Liệu' để bắt đầu.");
        } else {
            for (Document doc : library.getDocuments()) {
                sb.append("Tiêu đề: ").append(doc.getTitle())
                        .append(" | Tác giả: ").append(doc.getAuthor())
                        .append(" | Số lượng khả dụng: ").append(doc.getAvailableQuantity())
                        .append("\n");
            }
        }
        showAlert(Alert.AlertType.INFORMATION, "Danh sách tài liệu", sb.toString());
    }

    private void addUser() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Thêm Người Dùng");

        TextField nameField = new TextField();
        TextField idField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Tên:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Mã thành viên:"), 0, 1);
        grid.add(idField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    return new User(nameField.getText(), idField.getText());
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi đầu vào", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            try {
                library.addUser(user);
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm người dùng: " + user.getName());
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        });
    }

    private void displayUsers() {
        StringBuilder sb = new StringBuilder();
        if (library.getUsers().isEmpty()) {
            sb.append("Chưa có người dùng nào. Nhấn 'Thêm Người Dùng' để bắt đầu.");
        } else {
            for (User user : library.getUsers()) {
                sb.append("Tên: ").append(user.getName())
                        .append(" | Mã: ").append(user.getMemberId())
                        .append(" | Số tài liệu mượn: ").append(user.getBorrowedCount())
                        .append("\n");
            }
        }
        showAlert(Alert.AlertType.INFORMATION, "Danh sách người dùng", sb.toString());
    }

    private void showDocumentDetails(Document doc) {
        showAlert(Alert.AlertType.INFORMATION, "Thông tin tài liệu",
                "Tiêu đề: " + doc.getTitle() + "\nTác giả: " + doc.getAuthor() +
                        "\nSố lượng tổng: " + doc.getQuantity() +
                        "\nSố lượng khả dụng: " + doc.getAvailableQuantity() +
                        "\nĐang mượn: " + (doc.getBorrowedBy().isEmpty() ? "Không" : String.join(", ", doc.getBorrowedBy())));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
