package com.example.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class DesignController {

    @FXML
    public Label t3;
    @FXML
    public Label t2;
    @FXML
    public Label t4;
    @FXML
    public Label t1;
    @FXML
    public Label t5;
    @FXML
    public Label t6;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField widthField1;
    @FXML
    private TextField heightField1;
    @FXML
    private TextField widthField2;
    @FXML
    private TextField heightField2;
    @FXML
    private ComboBox<String> shapeComboBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Canvas designCanvas;

    private Stage primaryStage;


    // Setter method for the primaryStage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {

        shapeComboBox.setValue("Rectangle"); // Default Value
        t2.setVisible(false);
        t4.setVisible(false);
        t5.setVisible(false);
        t6.setVisible(false);
        widthField1.setVisible(false);
        heightField1.setVisible(false);
        widthField2.setVisible(false);
        heightField2.setVisible(false);
        widthField.setText(Double.toString(0));
        heightField.setText(Double.toString(0));
        widthField1.setText(Double.toString(0));
        heightField1.setText(Double.toString(0));
        widthField2.setText(Double.toString(0));
        heightField2.setText(Double.toString(0));
    }

    @FXML
    private void handleButtonAction() {
        String shape = shapeComboBox.getValue();
        if (shape.equals("L-Shape")) {
            t2.setVisible(true);
            t4.setVisible(true);
            t5.setVisible(true);
            t6.setVisible(true);
            widthField1.setVisible(true);
            heightField1.setVisible(true);
            widthField2.setVisible(true);
            heightField2.setVisible(true);
            t1.setText("Top Wall");
            t2.setText("Right Wall 01");
            t3.setText("Top Wall 02");
            t4.setText("Right Wall 02");
            t5.setText("Bottom Wall");
            t6.setText("Left Wall");
        } else {
            t2.setVisible(false);
            t4.setVisible(false);
            t5.setVisible(false);
            t6.setVisible(false);
            widthField1.setVisible(false);
            heightField1.setVisible(false);
            widthField2.setVisible(false);
            heightField2.setVisible(false);
            t1.setText("Width (px):");
            t3.setText("Height (px):");
        }
    }

    @FXML
    private void handleWallSizeAction() {
        String shape = shapeComboBox.getValue();
        if (shape.equals("L-Shape")) {
            double TopWall1 = Double.parseDouble(widthField.getText());
            double TopWall2 = Double.parseDouble(heightField.getText());
            double RightWall1 = Double.parseDouble(widthField1.getText());
            double RightWall2 = Double.parseDouble(heightField1.getText());

            widthField2.setText(Double.toString(TopWall1 + TopWall2));
            heightField2.setText(Double.toString(RightWall1 + RightWall2));
        } else if (shape.equals("Square")) {
            double width = Double.parseDouble(widthField.getText());
            heightField.setText(Double.toString(width));
        }
    }

    @FXML
    private void applyRoomSettings() {
        try {
            double width = Double.parseDouble(widthField.getText());
            double height = Double.parseDouble(heightField.getText());
            Color wallColor = colorPicker.getValue();
            String shape = shapeComboBox.getValue();

            GraphicsContext gc = designCanvas.getGraphicsContext2D();

            // Clear the canvas
            gc.clearRect(0, 0, designCanvas.getWidth(), designCanvas.getHeight());

            // Set wall color
            gc.setFill(wallColor);

            // Wall thickness
            double wallThickness = 5;
            gc.setLineWidth(wallThickness);

            if ("Rectangle".equals(shape)) {
                gc.fillRect(0, 0, width, wallThickness); // Top Wall
                gc.fillRect(0, height - wallThickness, width, wallThickness); // Bottom Wall
                gc.fillRect(0, 0, wallThickness, height); // Left Wall
                gc.fillRect(width - wallThickness, 0, wallThickness, height); // Right Wall

            } else if ("L-Shape".equals(shape)) {
                double width1 = Double.parseDouble(widthField1.getText());
                double height1 = Double.parseDouble(heightField1.getText());
                double width2 = Double.parseDouble(widthField2.getText());
                double height2 = Double.parseDouble(heightField2.getText());

                double x0 = 50, y0 = 50;
                double x1 = 50 + width, y1 = 50;
                double y2 = 50 + width1;
                double x3 = x1 + height;
                double y4 = y2 + height1;
                double x5 = x3 - width2;
                double y6 = y4 - height2;

                gc.strokeLine(x0, y0, x1, y1); // Top
                gc.strokeLine(x1, y1, x1, y2); // Right
                gc.strokeLine(x1, y2, x3, y2); // Bottom part 1
                gc.strokeLine(x3, y2, x3, y4); // Inner vertical
                gc.strokeLine(x3, y4, x5, y4); // Bottom part 2
                gc.strokeLine(x5, y4, x5, y6); // Left

            } else if ("Square".equals(shape)) {
                gc.fillRect(0, 0, width, wallThickness); // Top Wall
                gc.fillRect(0, width - wallThickness, width, wallThickness); // Bottom Wall
                gc.fillRect(0, 0, wallThickness, width); // Left Wall
                gc.fillRect(width - wallThickness, 0, wallThickness, width); // Right Wall
            }

        } catch (NumberFormatException e) {
            showAlert();
        }
    }

    @FXML
    private void viewIn3D() {
        String shape = shapeComboBox.getValue();
        // Get values from input fields
        double width = Double.parseDouble(widthField.getText()); // Main width
        double height = Double.parseDouble(heightField.getText()); // Main height
        double width1 = Double.parseDouble(widthField1.getText()); // Inner width
        double height1 = Double.parseDouble(heightField1.getText()); // Inner height
        double width2 = Double.parseDouble(widthField2.getText()); // Bottom width
        double height2 = Double.parseDouble(heightField2.getText()); // Bottom height
        Color wallColor = colorPicker.getValue();
        switch (shape) {
            case "L-Shape" -> {
                try {

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/3DDesign.fxml"));
                        Parent root = loader.load();

                        ThreeDDesignController controller = loader.getController();
                        controller.initialize3D(shape, width, height, width1, height1, width2, height2, wallColor);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (NumberFormatException e) {
                    showAlert();
                }
            }
            case "Rectangle" -> {
                try {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/3DDesign.fxml"));
                        Parent root = loader.load();

                        ThreeDDesignController controller = loader.getController();
                        controller.initialize3D(shape, width, height, width1, height1, width2, height2, wallColor);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (NumberFormatException e) {
                    showAlert();
                }
            }
            case "Square" -> {
                try {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/3DDesign.fxml"));
                        Parent root = loader.load();

                        ThreeDDesignController controller = loader.getController();
                        controller.initialize3D(shape, width, height, width1, height1, width2, height2, wallColor);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (NumberFormatException e) {
                    showAlert();
                }
            }
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid size");
        alert.setHeaderText(null);
        alert.setContentText("Please enter numeric values for width and height.");
        alert.showAndWait();
    }
}
