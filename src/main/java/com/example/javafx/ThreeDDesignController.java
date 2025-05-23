package com.example.javafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import org.fxyz3d.importers.obj.ObjImporter;
import org.fxyz3d.importers.Model3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThreeDDesignController {
    @FXML
    public ImageView sofaIcon;
    @FXML
    public ImageView sofaIcon1;
    @FXML
    public ImageView tableIcon;
    @FXML
    public ImageView chairIcon;
    @FXML
    public ImageView Chair2;
    @FXML
    public ImageView chairIcon3;
    @FXML
    private AnchorPane designPane;
    @FXML
    private VBox furnitureVBox;

    private Group roomGroup;
    private PerspectiveCamera camera;
    private Rotate rotateX;
    private Rotate rotateY;
    private Group furnitureGroup = new Group();
    private List<Box> furnitureList = new ArrayList<>();

    public void initialize() {
        sofaIcon.setOnMouseClicked(event -> addFurnitureToRoom("Sofa"));
        tableIcon.setOnMouseClicked(event -> addFurnitureToRoom("Table"));
        chairIcon.setOnMouseClicked(event -> addFurnitureToRoom("Chair"));
        Chair2.setOnMouseClicked(event -> addFurnitureToRoom("Chair1"));
        sofaIcon1.setOnMouseClicked(event -> addFurnitureToRoom("Sofa1"));
        chairIcon3.setOnMouseClicked(event -> addFurnitureToRoom("Chair3"));
        roomGroup = new Group();
        initializeRoom();
    }

    private Node load3DModel(String modelPath, String texturePath) {
        try {
            ObjImporter importer = new ObjImporter();
            Model3D model = importer.load(getClass().getResource(modelPath));
            Node modelNode = model.getRoot();

            // Apply texture to Shape3D
            if (modelNode instanceof Group) {
                applyTextureToGroup((Group) modelNode, texturePath);
            } else if (modelNode instanceof Shape3D) {
                applyTexture((Shape3D) modelNode, texturePath);
            }

            return modelNode;
        } catch (Exception e) {
            System.err.println("Error loading 3D model: " + e.getMessage());
            Box fallbackBox = new Box(50, 50, 50);
            fallbackBox.setMaterial(new PhongMaterial(Color.RED));
            return fallbackBox;
        }
    }

    private void applyTextureToGroup(Group group, String texturePath) {
        for (Node node : group.getChildren()) {
            if (node instanceof Group) {
                applyTextureToGroup((Group) node, texturePath);
            } else if (node instanceof Shape3D) {
                applyTexture((Shape3D) node, texturePath);
            }
        }
    }

    private void applyTexture(Shape3D shape, String texturePath) {
        PhongMaterial material = new PhongMaterial();
        try {
            Image textureImage = new Image(getClass().getResourceAsStream(texturePath));
            material.setDiffuseMap(textureImage);
        } catch (Exception e) {
            System.err.println("Error loading texture: " + e.getMessage());
            material.setDiffuseColor(Color.TAN); // Fallback color
        }
        shape.setMaterial(material);
    }

    private void addFurnitureToRoom(String furnitureType) {
        //model and texture paths for each furniture type
        String modelPath;
        String texturePath;

        switch (furnitureType) {
            case "Sofa":
                modelPath = "/models/OBJ.obj";
                texturePath = "/textures/sofa_texture.jpg";
                break;
            case "Table":
                modelPath = "/models/Wooden_Table.obj";
                texturePath = "/textures/wood_texture.jpg";
                break;
            case "Chair":
                modelPath = "/models/old_chair.obj";
                texturePath = "/textures/old_chair_texture.png";
                break;
            case "Chair1":
                modelPath = "/models/ArchWave_Chair.obj";
                texturePath = "/textures/ArchWave_Chair_texture.png";
                break;
            case "Sofa1":
                modelPath = "/models/Sofa.obj";
                texturePath = "/textures/Sofa2_texture.png";
                break;
            case "Chair3":
                modelPath = "/models/Chair_textile.obj";
                texturePath = "/textures/chair3_texture.JPG";
                break;
            default:
                modelPath = null;
                texturePath = null;
        }

        if (modelPath != null) {
            Node furnitureModel = load3DModel(modelPath, texturePath);

            if (furnitureModel != null) {
                // Set initial position and scale
                furnitureModel.setTranslateX(100);
                furnitureModel.setTranslateY(0);
                furnitureModel.setTranslateZ(50);
                furnitureModel.setScaleX(0.5);
                furnitureModel.setScaleY(0.5);
                furnitureModel.setScaleZ(0.5);

                // Add click handler
                furnitureModel.setOnMouseClicked(event -> {
                    selectedFurniture = furnitureModel;
                    event.consume();
                });

                // Add to the scene
                furnitureGroup.getChildren().add(furnitureModel);
                roomGroup.getChildren().add(furnitureGroup);

                // Enable movement for this furniture
                enable3DMovement(furnitureModel);
                designPane.requestFocus();
            }
        }
    }

    private void create3DGrid() {
        double gridSize = 50;  // Define grid size

        // Create horizontal grid lines
        for (double z = -300; z <= 300; z += gridSize) {
            Line line = new Line();
            line.setStartX(-300);
            line.setStartY(0);
            line.setEndX(300);
            line.setEndY(0);
            line.setTranslateZ(z);
            line.setStroke(Color.LIGHTGRAY);
            line.setStrokeWidth(0.5);
            roomGroup.getChildren().add(line);
        }

        // Create vertical grid lines
        for (double x = -300; x <= 300; x += gridSize) {
            Line line = new Line();
            line.setStartX(0);
            line.setStartY(-300);
            line.setEndX(0);
            line.setEndY(300);
            line.setTranslateX(x);
            line.setStroke(Color.LIGHTGRAY);
            line.setStrokeWidth(0.5);
            roomGroup.getChildren().add(line);
        }
    }

    private Node selectedFurniture;

    private void enable3DMovement(Node furniture) {
        selectedFurniture = furniture;

        final double gridSize = 5;  //grid size
        final double rotationStep = 5.0; // Degrees to rotate per key press
        final double scaleStep = 0.1; // Scale adjustment step

        // Initialize rotation transforms
        if (selectedFurniture.getTransforms().stream().noneMatch(t -> t instanceof Rotate && ((Rotate)t).getAxis() == Rotate.X_AXIS)) {
            Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
            Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
            Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);
            selectedFurniture.getTransforms().addAll(xRotate, yRotate, zRotate);
        }

        // Handle movement based on keyboard input
        designPane.setOnKeyPressed(event -> {
            if (selectedFurniture == null) return;

            double deltaX = 0;
            double deltaY = 0;
            double deltaZ = 0;
            double rotateX = 0;
            double rotateY = 0;
            double rotateZ = 0;
            double scaleChange = 0;

            switch (event.getCode()) {
                case NUMPAD8:
                    deltaZ = -gridSize;
                    break;
                case NUMPAD2:
                    deltaZ = gridSize;
                    break;
                case NUMPAD4:
                    deltaX = -gridSize;
                    break;
                case NUMPAD6:
                    deltaX = gridSize;
                    break;
                case W:
                    deltaY = -gridSize;
                    break;
                case S:
                    deltaY = gridSize;
                    break;
                case A:
                    rotateY = -rotationStep; // Rotate around Y-axis
                    break;
                case D:
                    rotateY = rotationStep; // Rotate around Y-axis
                    break;
                case Q:
                    rotateX = -rotationStep; // Rotate around X-axis
                    break;
                case E:
                    rotateX = rotationStep; // Rotate around X-axis
                    break;
                case Z:
                    rotateZ = -rotationStep; // Rotate around Z-axis
                    break;
                case C:
                    rotateZ = rotationStep; // Rotate around Z-axis
                    break;
                case PAGE_UP:
                    scaleChange = scaleStep;
                    break;
                case PAGE_DOWN:
                    scaleChange = -scaleStep;
                    break;
                case DELETE: removeSelectedFurniture(); break;
            }

            // Update position
            selectedFurniture.setTranslateX(selectedFurniture.getTranslateX() + deltaX);
            selectedFurniture.setTranslateY(selectedFurniture.getTranslateY() + deltaY);
            selectedFurniture.setTranslateZ(selectedFurniture.getTranslateZ() + deltaZ);

            // Update rotation
            if (rotateX != 0) {
                double finalRotateX = rotateX;
                selectedFurniture.getTransforms().stream()
                        .filter(t -> t instanceof Rotate && ((Rotate)t).getAxis() == Rotate.X_AXIS)
                        .findFirst()
                        .ifPresent(rotate -> ((Rotate)rotate).setAngle(((Rotate)rotate).getAngle() + finalRotateX));
            }
            if (rotateY != 0) {
                double finalRotateY = rotateY;
                selectedFurniture.getTransforms().stream()
                        .filter(t -> t instanceof Rotate && ((Rotate)t).getAxis() == Rotate.Y_AXIS)
                        .findFirst()
                        .ifPresent(rotate -> ((Rotate)rotate).setAngle(((Rotate)rotate).getAngle() + finalRotateY));
            }
            if (rotateZ != 0) {
                double finalRotateZ = rotateZ;
                selectedFurniture.getTransforms().stream()
                        .filter(t -> t instanceof Rotate && ((Rotate)t).getAxis() == Rotate.Z_AXIS)
                        .findFirst()
                        .ifPresent(rotate -> ((Rotate)rotate).setAngle(((Rotate)rotate).getAngle() + finalRotateZ));
            }

            // Update scale
            if (scaleChange != 0) {
                double newScaleX = selectedFurniture.getScaleX() + scaleChange;
                double newScaleY = selectedFurniture.getScaleY() + scaleChange;
                double newScaleZ = selectedFurniture.getScaleZ() + scaleChange;

                // Prevent scaling to zero or negative values
                if (newScaleX > 0.1 && newScaleY > 0.1 && newScaleZ > 0.1) {
                    selectedFurniture.setScaleX(newScaleX);
                    selectedFurniture.setScaleY(newScaleY);
                    selectedFurniture.setScaleZ(newScaleZ);
                }
            }

            // Constrain to grid
            selectedFurniture.setTranslateX(Math.round(selectedFurniture.getTranslateX() / gridSize) * gridSize);
            selectedFurniture.setTranslateY(Math.round(selectedFurniture.getTranslateY() / gridSize) * gridSize);
            selectedFurniture.setTranslateZ(Math.round(selectedFurniture.getTranslateZ() / gridSize) * gridSize);
        });

        designPane.requestFocus();
    }

    private void removeSelectedFurniture() {
        if (selectedFurniture != null) {
            furnitureGroup.getChildren().remove(selectedFurniture);
            furnitureList.remove(selectedFurniture);
            selectedFurniture = null;
        }
    }

    private void initializeRoom() {
        create3DGrid();  // Create the 3D grid on the floor

        // Create furniture and enable movement
        Box furniture = new Box(50, 50, 50);
        furniture.setTranslateX(100);
        furniture.setTranslateY(50);
        furniture.setTranslateZ(100);
        roomGroup.getChildren().add(furniture);

        enable3DMovement(furniture);  // Enable keyboard movement for the furniture
    }

    public void initialize3D(String shapeType, double width, double height, double width1, double height1, double width2, double height2, Color wallColor) {
        roomGroup = new Group();

        // Setup camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-800);
        camera.setNearClip(0.1);
        camera.setFarClip(1000);

        SubScene subScene = new SubScene(roomGroup, 600, 400, true, SceneAntialiasing.BALANCED);
        subScene.widthProperty().bind(designPane.widthProperty());
        subScene.heightProperty().bind(designPane.heightProperty());

        subScene.setFill(Color.WHITE);
        subScene.setCamera(camera);

        // Add textures to the walls
        PhongMaterial wallMaterial = new PhongMaterial();
        wallMaterial.setDiffuseColor(wallColor);

        if (shapeType.equals("L-Shape")) {
            Box topWall = new Box(width, 150, 15);
            topWall.setTranslateX(width / 2);
            topWall.setTranslateY(75);
            topWall.setTranslateZ(-7.5);
            topWall.setMaterial(wallMaterial);

            Box rightWall1 = new Box(15, 150, width1);
            rightWall1.setTranslateX(width - 7.5);
            rightWall1.setTranslateY(75);
            rightWall1.setTranslateZ(width1 / 2);
            rightWall1.setMaterial(wallMaterial);

            Box bottomWall1 = new Box(height, 150, 15);
            bottomWall1.setTranslateX(width + height / 2);
            bottomWall1.setTranslateY(75);
            bottomWall1.setTranslateZ(width1 - 7.5);
            bottomWall1.setMaterial(wallMaterial);

            Box innerVerticalWall = new Box(15, 150, height1);
            innerVerticalWall.setTranslateX(width + height - 7.5);
            innerVerticalWall.setTranslateY(75);
            innerVerticalWall.setTranslateZ(width1 + height1 / 2);
            innerVerticalWall.setMaterial(wallMaterial);

            // floor sections for L-shape
            PhongMaterial floorMaterial = new PhongMaterial(Color.DARKGRAY);// floor color

            Box floor1 = new Box(width, 15, height2);
            floor1.setTranslateX(width / 2);
            floor1.setTranslateY(7.5);
            floor1.setTranslateZ(height2 / 2);
            floor1.setMaterial(floorMaterial);

            Box floor2 = new Box(height, 15, height1);
            floor2.setTranslateX(width + height / 2);
            floor2.setTranslateY(7.5);
            floor2.setTranslateZ(width1 + height1 / 2);
            floor2.setMaterial(floorMaterial);


            roomGroup.getChildren().addAll(topWall, rightWall1, bottomWall1, innerVerticalWall, floor1, floor2);
        } else {
            Box backWall = new Box(width, 150, 15);
            backWall.setTranslateX(width / 2);
            backWall.setTranslateY(75);
            backWall.setTranslateZ(-height / 2);
            backWall.setMaterial(wallMaterial);

            Box leftWall = new Box(15, 150, height);
            leftWall.setTranslateX(7.5);
            leftWall.setTranslateY(75);
            leftWall.setTranslateZ(0);
            leftWall.setMaterial(wallMaterial);

            Box rightWall = new Box(15, 150, height);
            rightWall.setTranslateX(width - 7.5);
            rightWall.setTranslateY(75);
            rightWall.setTranslateZ(0);
            rightWall.setMaterial(wallMaterial);

            // single floor for rectangle
            PhongMaterial floorMaterial = new PhongMaterial(Color.DARKGRAY);//floor color

            Box floor = new Box(width, 15, height);
            floor.setTranslateX(width / 2);
            floor.setTranslateY(7.5);
            floor.setTranslateZ(0);
            floor.setMaterial(floorMaterial);

            roomGroup.getChildren().addAll(backWall, leftWall, rightWall, floor);
        }

        //Rotate
        rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        roomGroup.getTransforms().addAll(rotateX, rotateY);

        //Mouse controls
        subScene.setOnMousePressed(event -> {
            roomGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        subScene.setOnMouseDragged(event -> {
            double[] mouseOld = (double[]) roomGroup.getUserData();
            double deltaX = event.getSceneX() - mouseOld[0];
            double deltaY = event.getSceneY() - mouseOld[1];
            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.1);
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.1);
            roomGroup.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        subScene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double zoomFactor = event.getDeltaY() > 0 ? -10 : 10;
            camera.setTranslateZ(camera.getTranslateZ() + zoomFactor);
        });

        //keyboard controls for camera movement
        subScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case NUMPAD8:
                    camera.setTranslateY(camera.getTranslateY() - 10); // Move up
                    break;
                case NUMPAD2:
                    camera.setTranslateY(camera.getTranslateY() + 10); // Move down
                    break;
                case NUMPAD4:
                    camera.setTranslateX(camera.getTranslateX() - 10); // Move left
                    break;
                case NUMPAD6:
                    camera.setTranslateX(camera.getTranslateX() + 10); // Move right
                    break;
            }
        });

        designPane.getChildren().add(subScene);
        subScene.setFocusTraversable(true);
        subScene.requestFocus();
    }

    @FXML
    private void saveDesignAsImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Design as Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showSaveDialog(designPane.getScene().getWindow());
        if (file != null) {
            try {
                //Get the SubScene from designPane
                SubScene subScene = (SubScene) designPane.getChildren().get(0);

                // Create a snapshot of the SubScene
                WritableImage image = subScene.snapshot(null, null);

                // Determine file format from extension
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();

                // Save the image
                switch (extension) {
                    case "png":
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                        break;
                    case "jpg":
                    case "jpeg":
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                        BufferedImage rgbImage = new BufferedImage(
                                bufferedImage.getWidth(),
                                bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB
                        );
                        rgbImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
                        ImageIO.write(rgbImage, "jpg", file);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported image format");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
