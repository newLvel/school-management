package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.Main;
import com.example.schoolmanagement.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        // Simple validation - in a real app, check hashed password
        if (isValid(username, password)) {
            try {
                Main.showMainApp();
            } catch (IOException e) {
                errorLabel.setText("Failed to load the main application.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    private boolean isValid(String username, String password) {
        // This is a simplified check. A real app should use password hashing.
        return userDAO.getAllUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && checkPassword(password, user.getPasswordHash()));
    }

    private boolean checkPassword(String plainPassword, String storedPassword) {
        // In this mock setup, we assume plain text passwords.
        // In a real app, this method would compare a hashed version of plainPassword
        // with the storedPassword hash.
        return plainPassword.equals(storedPassword);
    }
}
