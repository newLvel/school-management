package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.ClassDAO;
import com.example.schoolmanagement.dao.ExamDAO;
import com.example.schoolmanagement.dao.SubjectDAO;
import com.example.schoolmanagement.models.Exam;
import com.example.schoolmanagement.models.SchoolClass;
import com.example.schoolmanagement.models.Subject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class ExamsController {

    @FXML private TableView<Exam> examTable;
    @FXML private TableColumn<Exam, Integer> colId;
    @FXML private TableColumn<Exam, String> colExamName;
    @FXML private TableColumn<Exam, LocalDate> colExamDate;
    @FXML private TableColumn<Exam, String> colClass;
    @FXML private TableColumn<Exam, Void> colActions;

    private final ExamDAO examDAO = new ExamDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadExams();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colExamName.setCellValueFactory(new PropertyValueFactory<>("examName"));
        colExamDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        addActionsToTable();
    }

    private void loadExams() {
        examTable.setItems(FXCollections.observableArrayList(examDAO.getAllExams()));
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button gradesBtn = new Button("Enter Grades");
            private final Button deleteBtn = new Button("Delete");
            {
                gradesBtn.getStyleClass().addAll("btn-primary", "action-button");
                deleteBtn.getStyleClass().addAll("danger-button", "action-button");
                gradesBtn.setOnAction(event -> handleEnterGrades(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(event -> {
                    Exam exam = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Exam");
                    alert.setHeaderText("Delete " + exam.getExamName() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        examDAO.deleteExam(exam.getId());
                        loadExams();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, gradesBtn, deleteBtn));
            }
        });
    }

    @FXML
    private void handleAddExam() {
        Dialog<Exam> dialog = new Dialog<>();
        dialog.setTitle("Schedule New Exam");
        dialog.setHeaderText(null);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField examNameField = new TextField();
        DatePicker datePicker = new DatePicker();
        ComboBox<SchoolClass> classBox = new ComboBox<>(FXCollections.observableArrayList(classDAO.getAllClasses()));
        classBox.setConverter(new StringConverter<>() {
            @Override public String toString(SchoolClass c) { return c == null ? "" : c.getFullClassName(); }
            @Override public SchoolClass fromString(String s) { return null; }
        });
        grid.add(new Label("Exam Name:"), 0, 0);
        grid.add(examNameField, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Class:"), 0, 2);
        grid.add(classBox, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                return new Exam(examNameField.getText(), datePicker.getValue(), classBox.getValue().getId());
            }
            return null;
        });
        Optional<Exam> result = dialog.showAndWait();
        result.ifPresent(exam -> {
            examDAO.addExam(exam);
            loadExams();
        });
    }

    private void handleEnterGrades(Exam exam) {
        if (exam == null) return;
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/schoolmanagement/fxml/grade_entry.fxml"));
            javafx.scene.Parent root = loader.load();
            GradeEntryController controller = loader.getController();
            controller.setExam(exam);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Enter Grades - " + exam.getExamName());
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageSubjects() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Manage Subjects");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        VBox content = new VBox(10);
        ListView<Subject> subjectListView = new ListView<>(FXCollections.observableArrayList(subjectDAO.getAllSubjects()));
        GridPane addPane = new GridPane();
        addPane.setHgap(5);
        TextField nameField = new TextField() {{ setPromptText("Subject Name"); }};
        TextField codeField = new TextField() {{ setPromptText("Code"); }};
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            if (!nameField.getText().isEmpty()) {
                subjectDAO.addSubject(new Subject(nameField.getText(), codeField.getText()));
                subjectListView.setItems(FXCollections.observableArrayList(subjectDAO.getAllSubjects()));
                nameField.clear();
                codeField.clear();
            }
        });
        addPane.addRow(0, nameField, codeField, addButton);
        content.getChildren().addAll(new Label("Existing Subjects:"), subjectListView, new Separator(), new Label("Add New Subject:"), addPane);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }
}
