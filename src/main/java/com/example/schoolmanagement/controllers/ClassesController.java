package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.ClassDAO;
import com.example.schoolmanagement.dao.TeacherDAO;
import com.example.schoolmanagement.models.SchoolClass;
import com.example.schoolmanagement.models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import java.io.IOException;
import java.util.Optional;

public class ClassesController {

    @FXML private TableView<SchoolClass> classTable;
    @FXML private TableColumn<SchoolClass, Integer> colId;
    @FXML private TableColumn<SchoolClass, String> colClassName;
    @FXML private TableColumn<SchoolClass, String> colSection;
    @FXML private TableColumn<SchoolClass, String> colTeacher;
    @FXML private TableColumn<SchoolClass, Void> colActions;
    @FXML private TextField searchField;

    private final ClassDAO classDAO = new ClassDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final ObservableList<SchoolClass> classList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadClasses();
        searchField.textProperty().addListener((obs, old, val) -> filterClasses(val));
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClassName.setCellValueFactory(new PropertyValueFactory<>("className"));
        colSection.setCellValueFactory(new PropertyValueFactory<>("section"));
        colTeacher.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        addActionsToTable();
    }

    private void loadClasses() {
        classList.clear();
        classList.addAll(classDAO.getAllClasses());
        classTable.setItems(classList);
    }

    private void filterClasses(String query) {
        if (query == null || query.isEmpty()) {
            classTable.setItems(classList);
            return;
        }
        ObservableList<SchoolClass> filteredList = FXCollections.observableArrayList();
        for (SchoolClass c : classList) {
            if (c.getClassName().toLowerCase().contains(query.toLowerCase()) ||
                c.getSection().toLowerCase().contains(query.toLowerCase()) ||
                c.getTeacherName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(c);
            }
        }
        classTable.setItems(filteredList);
    }

    @FXML
    private void handleAddClass() {
        showClassDialog(null);
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final Button viewBtn = new Button("View");
            {
                editBtn.getStyleClass().addAll("btn-primary", "action-button");
                deleteBtn.getStyleClass().addAll("danger-button", "action-button");
                viewBtn.getStyleClass().addAll("btn-primary", "action-button");

                editBtn.setOnAction(event -> showClassDialog(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(event -> handleDeleteClass(getTableView().getItems().get(getIndex())));
                viewBtn.setOnAction(event -> handleViewDetails(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, viewBtn, editBtn, deleteBtn));
                }
            }
        });
    }

    private void handleDeleteClass(SchoolClass schoolClass) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Class");
        alert.setHeaderText("Are you sure you want to delete " + schoolClass.getFullClassName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            classDAO.deleteClass(schoolClass.getId());
            loadClasses();
        }
    }

    private void handleViewDetails(SchoolClass schoolClass) {
        if (schoolClass == null) return;
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/schoolmanagement/fxml/class_details.fxml"));
            javafx.scene.Parent root = loader.load();
            ClassDetailsController controller = loader.getController();
            controller.setClass(schoolClass);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Class Details - " + schoolClass.getFullClassName());
            stage.setScene(new javafx.scene.Scene(root, 600, 500));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showClassDialog(SchoolClass schoolClass) {
        Dialog<SchoolClass> dialog = new Dialog<>();
        dialog.setTitle(schoolClass == null ? "Add New Class" : "Edit Class");
        dialog.setHeaderText(null);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField classNameField = new TextField();
        TextField sectionField = new TextField();
        ComboBox<Teacher> teacherComboBox = new ComboBox<>();
        
        teacherComboBox.setItems(FXCollections.observableArrayList(teacherDAO.getAllTeachers()));
        teacherComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Teacher object) {
                return object == null ? "" : object.getFullName();
            }
            @Override
            public Teacher fromString(String string) { return null; }
        });

        if (schoolClass != null) {
            classNameField.setText(schoolClass.getClassName());
            sectionField.setText(schoolClass.getSection());
            for (Teacher t : teacherComboBox.getItems()) {
                if (t.getId() == schoolClass.getTeacherId()) {
                    teacherComboBox.getSelectionModel().select(t);
                    break;
                }
            }
        }

        grid.add(new Label("Class Name:"), 0, 0);
        grid.add(classNameField, 1, 0);
        grid.add(new Label("Section:"), 0, 1);
        grid.add(sectionField, 1, 1);
        grid.add(new Label("Assigned Teacher:"), 0, 2);
        grid.add(teacherComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (classNameField.getText().isEmpty() || sectionField.getText().isEmpty()) {
                    return null;
                }
                int teacherId = teacherComboBox.getValue() != null ? teacherComboBox.getValue().getId() : 0;
                if (schoolClass == null) {
                    return new SchoolClass(classNameField.getText(), sectionField.getText(), teacherId);
                } else {
                    schoolClass.setClassName(classNameField.getText());
                    schoolClass.setSection(sectionField.getText());
                    schoolClass.setTeacherId(teacherId);
                    return schoolClass;
                }
            }
            return null;
        });

        Optional<SchoolClass> result = dialog.showAndWait();
        result.ifPresent(res -> {
            if (schoolClass == null) {
                classDAO.addClass(res);
            } else {
                classDAO.updateClass(res);
            }
            loadClasses();
        });
    }
     private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
