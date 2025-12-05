package com.example.schoolmanagement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLayoutController {

    @FXML private StackPane contentArea;
    @FXML private TextField globalSearchField;

    @FXML
    public void initialize() {
        showDashboard();
        
        // Bind Enter key on search field
        if (globalSearchField != null) {
            globalSearchField.setOnAction(e -> handleGlobalSearch());
        }
    }

    private void handleGlobalSearch() {
        String query = globalSearchField.getText();
        if (query != null && !query.trim().isEmpty()) {
            // Default behavior: Search in Students list
            loadViewWithSearch("/com/example/schoolmanagement/fxml/student_list.fxml", query);
        }
    }

    @FXML
    private void showDashboard() {
        loadView("/com/example/schoolmanagement/fxml/dashboard.fxml");
    }

    @FXML
    private void showStudents() {
        loadView("/com/example/schoolmanagement/fxml/student_list.fxml");
    }
    
    // ... other methods ...

    @FXML
    private void showTeachers() {
        loadView("/com/example/schoolmanagement/fxml/teachers.fxml");
    }

    @FXML
    private void showClasses() {
        loadView("/com/example/schoolmanagement/fxml/classes.fxml");
    }

    @FXML
    private void showParents() {
        loadView("/com/example/schoolmanagement/fxml/parents.fxml");
    }

    @FXML
    private void showExams() {
        loadView("/com/example/schoolmanagement/fxml/exams.fxml");
    }

    @FXML
    private void showTransport() {
        loadView("/com/example/schoolmanagement/fxml/transport.fxml");
    }

    @FXML
    private void showSettings() {
        loadView("/com/example/schoolmanagement/fxml/settings.fxml");
    }

    private void loadView(String fxmlPath) {
        loadViewWithSearch(fxmlPath, null);
    }

    private void loadViewWithSearch(String fxmlPath, String searchQuery) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Pass search query if applicable
            if (searchQuery != null) {
                Object controller = loader.getController();
                if (controller instanceof StudentListController) {
                    ((StudentListController) controller).setSearchQuery(searchQuery);
                }
                // Can extend to TeacherController etc.
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, "Failed to load view: " + fxmlPath, e);
        }
    }
}
