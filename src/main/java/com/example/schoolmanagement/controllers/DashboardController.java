package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.FeeDAO;
import com.example.schoolmanagement.dao.ParentDAO;
import com.example.schoolmanagement.dao.StudentDAO;
import com.example.schoolmanagement.dao.TeacherDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label studentCountLabel;
    @FXML private Label teacherCountLabel;
    @FXML private Label parentCountLabel;
    @FXML private Label earningsLabel;
    @FXML private BarChart<String, Number> earningsChart;
    @FXML private PieChart studentsPieChart;

    private final StudentDAO studentDAO = new StudentDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final ParentDAO parentDAO = new ParentDAO();
    private final FeeDAO feeDAO = new FeeDAO();

    @FXML
    public void initialize() {
        loadStatistics();
        setupCharts();
    }

    private void loadStatistics() {
        studentCountLabel.setText(String.valueOf(studentDAO.getStudentCount()));
        teacherCountLabel.setText(String.valueOf(teacherDAO.getTeacherCount()));
        parentCountLabel.setText(String.valueOf(parentDAO.getParentCount()));
        earningsLabel.setText(String.format("$%.2f", feeDAO.getTotalEarnings()));
    }

    private void setupCharts() {
        // Setup Earnings Chart with real data
        XYChart.Series<String, Number> earningsSeries = new XYChart.Series<>();
        earningsSeries.setName("Earnings");
        earningsSeries.setData(feeDAO.getMonthlyEarningsForYear(2023));
        if (earningsChart.getData().isEmpty()) {
            earningsChart.getData().add(earningsSeries);
        }

        // Setup Students Pie Chart
        studentsPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Male", studentDAO.getStudentCountByGender("Male")),
                new PieChart.Data("Female", studentDAO.getStudentCountByGender("Female"))
        ));
    }
}
