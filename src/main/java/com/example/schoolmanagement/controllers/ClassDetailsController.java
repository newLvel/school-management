package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.StudentDAO;
import com.example.schoolmanagement.models.SchoolClass;
import com.example.schoolmanagement.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClassDetailsController {

    @FXML private Label classNameLabel;
    @FXML private Label teacherLabel;
    
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colGender;
    @FXML private TableColumn<Student, String> colParent;

    private final StudentDAO studentDAO = new StudentDAO();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void setClass(SchoolClass schoolClass) {
        classNameLabel.setText("Class: " + schoolClass.getFullClassName());
        teacherLabel.setText("Teacher: " + schoolClass.getTeacherName());
        
        loadStudents(schoolClass.getId());
    }

    private void loadStudents(int classId) {
        studentList.clear();
        studentList.addAll(studentDAO.getStudentsByClassId(classId));
        studentTable.setItems(studentList);
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colParent.setCellValueFactory(new PropertyValueFactory<>("parentName"));
    }
}
