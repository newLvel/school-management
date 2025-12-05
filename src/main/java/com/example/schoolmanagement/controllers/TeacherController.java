package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.TeacherDAO;
import com.example.schoolmanagement.models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.util.Optional;

public class TeacherController {

    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, String> colFirstName;
    @FXML private TableColumn<Teacher, String> colLastName;
    @FXML private TableColumn<Teacher, String> colEmail;
    @FXML private TableColumn<Teacher, String> colPhone;
    @FXML private TableColumn<Teacher, String> colSpecialization;
    @FXML private TableColumn<Teacher, Void> colActions;

    @FXML private TextField searchField;

    private final TeacherDAO teacherDAO = new TeacherDAO();
    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadTeachers();
        searchField.textProperty().addListener((obs, old, val) -> filterTeachers(val));
    }

    private void setupTableColumns() {
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        addActionsToTable();
    }

    private void loadTeachers() {
        teacherList.setAll(teacherDAO.getAllTeachers());
        teacherTable.setItems(teacherList);
    }

    private void filterTeachers(String query) {
        if (query == null || query.isEmpty()) {
            teacherTable.setItems(teacherList);
            return;
        }
        teacherTable.setItems(teacherList.filtered(t ->
            t.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
            t.getLastName().toLowerCase().contains(query.toLowerCase()) ||
            t.getEmail().toLowerCase().contains(query.toLowerCase())
        ));
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.getStyleClass().addAll("btn-primary", "action-button");
                deleteBtn.getStyleClass().addAll("danger-button", "action-button");

                editBtn.setOnAction(event -> {
                    Teacher teacher = getTableView().getItems().get(getIndex());
                    showTeacherDialog(teacher);
                });

                deleteBtn.setOnAction(event -> {
                    Teacher teacher = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Teacher");
                    alert.setHeaderText("Are you sure you want to delete " + teacher.getFullName() + "?");
                    alert.setContentText("This action cannot be undone.");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        teacherDAO.deleteTeacher(teacher.getId());
                        loadTeachers();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editBtn, deleteBtn));
                }
            }
        });
    }

    @FXML
    private void handleAddTeacher() {
        showTeacherDialog(null);
    }

    private void handleDeleteTeacher(Teacher teacher) {
        if (teacher != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + teacher.getFullName() + "?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    teacherDAO.deleteTeacher(teacher.getId());
                    loadTeachers();
                }
            });
        }
    }

    private void showTeacherDialog(Teacher teacher) {
        Dialog<Teacher> dialog = new Dialog<>();
        dialog.setTitle(teacher == null ? "Add New Teacher" : "Edit Teacher");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField specializationField = new TextField();
        DatePicker hireDatePicker = new DatePicker();

        if (teacher != null) {
            firstNameField.setText(teacher.getFirstName());
            lastNameField.setText(teacher.getLastName());
            emailField.setText(teacher.getEmail());
            phoneField.setText(teacher.getPhone());
            specializationField.setText(teacher.getSpecialization());
            hireDatePicker.setValue(teacher.getHireDate());
        }

        grid.add(new Label("First Name:"), 0, 0); grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1); grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2); grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3); grid.add(phoneField, 1, 3);
        grid.add(new Label("Specialization:"), 0, 4); grid.add(specializationField, 1, 4);
        grid.add(new Label("Hire Date:"), 0, 5); grid.add(hireDatePicker, 1, 5);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (teacher == null) {
                    return new Teacher(firstNameField.getText(), lastNameField.getText(), emailField.getText(), phoneField.getText(), specializationField.getText(), hireDatePicker.getValue());
                } else {
                    teacher.setFirstName(firstNameField.getText());
                    teacher.setLastName(lastNameField.getText());
                    teacher.setEmail(emailField.getText());
                    teacher.setPhone(phoneField.getText());
                    teacher.setSpecialization(specializationField.getText());
                    teacher.setHireDate(hireDatePicker.getValue());
                    return teacher;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(t -> {
            if (teacher == null) teacherDAO.addTeacher(t);
            else teacherDAO.updateTeacher(t);
            loadTeachers();
        });
    }
}