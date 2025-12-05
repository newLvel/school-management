package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.GradeDAO;
import com.example.schoolmanagement.dao.SubjectDAO;
import com.example.schoolmanagement.models.Exam;
import com.example.schoolmanagement.models.Grade;
import com.example.schoolmanagement.models.Subject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

public class GradeEntryController {

    @FXML private Label examTitleLabel;
    @FXML private Label classLabel;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private TableView<Grade> gradeTable;
    @FXML private TableColumn<Grade, String> colStudentName;
    @FXML private TableColumn<Grade, Double> colScore;
    @FXML private TableColumn<Grade, String> colRemarks;

    private Exam currentExam;
    private final GradeDAO gradeDAO = new GradeDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private ObservableList<Grade> gradeList = FXCollections.observableArrayList();

    public void setExam(Exam exam) {
        this.currentExam = exam;
        examTitleLabel.setText("Grading: " + exam.getExamName());
        classLabel.setText("Class: " + exam.getClassName());
        loadSubjects();
    }

    private void loadSubjects() {
        subjectComboBox.setItems(FXCollections.observableArrayList(subjectDAO.getAllSubjects()));
        subjectComboBox.setConverter(new StringConverter<Subject>() {
            @Override
            public String toString(Subject object) { return object == null ? "" : object.getSubjectName(); }
            @Override
            public Subject fromString(String string) { return null; }
        });
        
        // Listener for subject change
        subjectComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadGrades(newVal);
            }
        });
    }

    private void loadGrades(Subject subject) {
        if (currentExam == null || subject == null) return;
        
        gradeList.clear();
        gradeList.addAll(gradeDAO.getGradesForExamSubject(currentExam.getId(), currentExam.getClassId(), subject.getId()));
        gradeTable.setItems(gradeList);
    }

    @FXML
    public void initialize() {
        setupTable();
    }

    private void setupTable() {
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        
        // Editable Score Column
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colScore.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colScore.setOnEditCommit(event -> {
            Grade grade = event.getRowValue();
            grade.setScore(event.getNewValue());
            saveGrade(grade);
        });
        
        // Editable Remarks Column
        colRemarks.setCellValueFactory(new PropertyValueFactory<>("remarks"));
        colRemarks.setCellFactory(TextFieldTableCell.forTableColumn());
        colRemarks.setOnEditCommit(event -> {
            Grade grade = event.getRowValue();
            grade.setRemarks(event.getNewValue());
            saveGrade(grade);
        });

        gradeTable.setEditable(true);
    }
    
    private void saveGrade(Grade grade) {
        if (currentExam == null || subjectComboBox.getValue() == null) return;
        
        gradeDAO.saveOrUpdateGrade(
            currentExam.getId(),
            grade.getStudentId(),
            subjectComboBox.getValue().getId(),
            grade.getScore(),
            grade.getRemarks()
        );
    }
}
