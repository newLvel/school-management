package com.example.schoolmanagement;

import com.example.schoolmanagement.database.MockDataInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showLoginScreen();
    }

    public static void showLoginScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        primaryStage.setTitle("EduManager - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showMainApp() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/main-layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        primaryStage.setTitle("EduManager - School Management System");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        // This will reset and populate the database every time the app starts
        MockDataInitializer.initialize();
        launch(args);
    }
}
