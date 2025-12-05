package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.ParentDAO;
import com.example.schoolmanagement.models.Parent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.Optional;

public class ParentsController {

    @FXML private TableView<Parent> parentTable;
    @FXML private TableColumn<Parent, Integer> colId;
    @FXML private TableColumn<Parent, String> colFatherName;
    @FXML private TableColumn<Parent, String> colMotherName;
    @FXML private TableColumn<Parent, String> colEmail;
    @FXML private TableColumn<Parent, String> colPhone;
    @FXML private TableColumn<Parent, Void> colActions;
    @FXML private TextField searchField;

    private final ParentDAO parentDAO = new ParentDAO();
    private ObservableList<Parent> parentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadParents();
        searchField.textProperty().addListener((obs, old, val) -> filterParents(val));
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFatherName.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        colMotherName.setCellValueFactory(new PropertyValueFactory<>("motherName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addActionsToTable();
    }

    private void loadParents() {
        parentList.clear();
        parentList.addAll(parentDAO.getAllParents());
        parentTable.setItems(parentList);
    }

    private void filterParents(String query) {
        if (query == null || query.isEmpty()) {
            parentTable.setItems(parentList);
            return;
        }
        ObservableList<Parent> filteredList = FXCollections.observableArrayList();
        String lowerQuery = query.toLowerCase();
        for (Parent p : parentList) {
            if ((p.getFatherName() != null && p.getFatherName().toLowerCase().contains(lowerQuery)) ||
                (p.getMotherName() != null && p.getMotherName().toLowerCase().contains(lowerQuery)) ||
                (p.getEmail() != null && p.getEmail().toLowerCase().contains(lowerQuery))) {
                filteredList.add(p);
            }
        }
        parentTable.setItems(filteredList);
    }

    @FXML
    private void handleAddParent() {
        showParentDialog(null);
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.getStyleClass().addAll("btn-primary", "action-button");
                deleteBtn.getStyleClass().addAll("danger-button", "action-button");
                editBtn.setOnAction(event -> showParentDialog(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(event -> {
                    Parent parent = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Parent");
                    alert.setHeaderText("Delete this parent record?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        parentDAO.deleteParent(parent.getId());
                        loadParents();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editBtn, deleteBtn));
            }
        });
    }

    private void showParentDialog(Parent parent) {
        Dialog<Parent> dialog = new Dialog<>();
        dialog.setTitle(parent == null ? "Add New Parent" : "Edit Parent");
        dialog.setHeaderText(null);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField fatherNameField = new TextField();
        TextField motherNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextArea addressArea = new TextArea();
        addressArea.setPrefRowCount(3);

        if (parent != null) {
            fatherNameField.setText(parent.getFatherName());
            motherNameField.setText(parent.getMotherName());
            emailField.setText(parent.getEmail());
            phoneField.setText(parent.getPhone());
            addressArea.setText(parent.getAddress());
        }

        grid.add(new Label("Father's Name:"), 0, 0);
        grid.add(fatherNameField, 1, 0);
        grid.add(new Label("Mother's Name:"), 0, 1);
        grid.add(motherNameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressArea, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (parent == null) {
                    return new Parent(fatherNameField.getText(), motherNameField.getText(), emailField.getText(), phoneField.getText(), addressArea.getText());
                } else {
                    parent.setFatherName(fatherNameField.getText());
                    parent.setMotherName(motherNameField.getText());
                    parent.setEmail(emailField.getText());
                    parent.setPhone(phoneField.getText());
                    parent.setAddress(addressArea.getText());
                    return parent;
                }
            }
            return null;
        });

        Optional<Parent> result = dialog.showAndWait();
        result.ifPresent(res -> {
            if (parent == null) {
                parentDAO.addParent(res);
            } else {
                parentDAO.updateParent(res);
            }
            loadParents();
        });
    }
}
