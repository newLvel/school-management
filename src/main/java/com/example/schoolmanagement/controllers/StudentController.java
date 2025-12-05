package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.ExamDAO;
import com.example.schoolmanagement.dao.StudentDAO;
import com.example.schoolmanagement.models.Exam;
import com.example.schoolmanagement.models.Student;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.chart.BarChart;

public class StudentController {

    @FXML private ImageView studentImageView;
    @FXML private Label studentNameLabel;
    @FXML private Label studentEmailLabel;
    @FXML private Label studentPhoneLabel;
    @FXML private Label parentNameLabel;
    @FXML private Label attendanceLabel;
    @FXML private Label assignmentsLabel;
    @FXML private Label overallGradeLabel;
    @FXML private BarChart<?, ?> performanceChart;
    @FXML private TableView<Exam> upcomingExamsTable;
    @FXML private TableColumn<Exam, String> colExamId;
    @FXML private TableColumn<Exam, String> colExamName;
    @FXML private TableColumn<Exam, String> colSubject;
    @FXML private TableColumn<Exam, String> colDate;

    private final StudentDAO studentDAO = new StudentDAO();
    private final ExamDAO examDAO = new ExamDAO();
        @FXML
        public void initialize() {
            // Data is now loaded via loadStudentData()
        }
        
        public void loadStudentData(int studentId) {
            Student student = studentDAO.getStudentById(studentId);
            if (student == null) return;
    
            studentNameLabel.setText(student.getFullName() + " (Class " + student.getClassName() + ")");
            parentNameLabel.setText("Parent: " + student.getParentName());
            
            // Mock data for other fields as they are not in the DB yet
            attendanceLabel.setText("92%");
            assignmentsLabel.setText("3");
            overallGradeLabel.setText("B+");
            
            // Load upcoming exams
            colExamId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colExamName.setCellValueFactory(new PropertyValueFactory<>("examName"));
            colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName")); // Assuming this exists on Exam model
            colDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));
            upcomingExamsTable.setItems(FXCollections.observableArrayList(examDAO.getExamsByClassId(student.getClassId())));
        }
    }
    