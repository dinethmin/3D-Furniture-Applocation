package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CartController {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private void handlecart(ActionEvent event) throws IOException {

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
    }
}
