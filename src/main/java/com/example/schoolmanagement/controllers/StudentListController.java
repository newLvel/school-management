package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.*;
import com.example.schoolmanagement.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class StudentListController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colClass;
    @FXML private TableColumn<Student, String> colParent;
    @FXML private TableColumn<Student, Void> colActions;
    @FXML private TextField searchField;

    private final StudentDAO studentDAO = new StudentDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadStudents();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterStudents(newVal));
    }

    public void setSearchQuery(String query) {
        searchField.setText(query);
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        colParent.setCellValueFactory(new PropertyValueFactory<>("parentName"));
        addActionsToTable();
    }

    private void loadStudents() {
        studentTable.setItems(FXCollections.observableArrayList(studentDAO.getAllStudents()));
    }

    private void filterStudents(String query) {
        if (query == null || query.isEmpty()) {
            loadStudents();
        } else {
            studentTable.setItems(FXCollections.observableArrayList(studentDAO.searchStudents(query)));
        }
    }

    @FXML
    private void handleCreateStudent() {
        showStudentDialog(null);
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                viewBtn.getStyleClass().add("action-button");
                editBtn.getStyleClass().add("action-button");
                deleteBtn.getStyleClass().add("danger-button");
                
                viewBtn.setOnAction(event -> showStudentProfile(getTableView().getItems().get(getIndex())));
                editBtn.setOnAction(event -> showStudentDialog(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Student");
                    alert.setHeaderText("Delete " + student.getFullName() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        studentDAO.deleteStudent(student.getId());
                        loadStudents();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, viewBtn, editBtn, deleteBtn));
            }
        });
    }

    private void showStudentProfile(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/schoolmanagement/fxml/students.fxml"));
            javafx.scene.Parent root = loader.load();
            StudentController controller = loader.getController();
            controller.loadStudentData(student.getId());
            Stage stage = new Stage();
            stage.setTitle("Student Profile");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showStudentDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle(student == null ? "Create New Student" : "Edit Student");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        DatePicker dobPicker = new DatePicker();
        ComboBox<String> genderBox = new ComboBox<>(FXCollections.observableArrayList("Male", "Female"));
        ComboBox<SchoolClass> classBox = new ComboBox<>(FXCollections.observableArrayList(new ClassDAO().getAllClasses()));
        ComboBox<Parent> parentBox = new ComboBox<>(FXCollections.observableArrayList(new ParentDAO().getAllParents()));

        if (student != null) {
            firstNameField.setText(student.getFirstName());
            lastNameField.setText(student.getLastName());
            dobPicker.setValue(student.getDob());
            genderBox.setValue(student.getGender());
            // Set combo box selections
        }

        grid.add(new Label("First Name:"), 0, 0); grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);  grid.add(lastNameField, 1, 1);
        grid.add(new Label("Birthday:"), 0, 2);   grid.add(dobPicker, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);     grid.add(genderBox, 1, 3);
        grid.add(new Label("Class:"), 0, 4);      grid.add(classBox, 1, 4);
        grid.add(new Label("Parent:"), 0, 5);     grid.add(parentBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Student resultStudent = (student == null) ? new Student() : student;
                resultStudent.setFirstName(firstNameField.getText());
                resultStudent.setLastName(lastNameField.getText());
                resultStudent.setDob(dobPicker.getValue());
                resultStudent.setGender(genderBox.getValue());
                if (classBox.getValue() != null) resultStudent.setClassId(classBox.getValue().getId());
                if (parentBox.getValue() != null) resultStudent.setParentId(parentBox.getValue().getId());
                return resultStudent;
            }
            return null;
        });

        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(s -> {
            if (student == null) {
                studentDAO.addStudent(s);
            } else {
                studentDAO.updateStudent(s);
            }
            loadStudents();
        });
    }
}