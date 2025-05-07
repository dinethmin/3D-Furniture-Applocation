package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage primaryStage;  // Add this field to store the primary stage

    // Setter method to set the primaryStage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check credentials (Replace with real authentication)
        if ("admin".equals(username) && "1234".equals(password)) {
            System.out.println("Login Successful! Switching to Design Screen.");

            // Get the current stage (login screen) and load the design screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/design.fxml"));
            Parent root = loader.load();

            // Get the controller of the design screen
            DesignController designController = loader.getController();

            // Pass the current stage to the design screen's controller
            designController.setPrimaryStage(primaryStage);

            // Set the new scene and show it
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("Invalid credentials!");
        }
    }
}
