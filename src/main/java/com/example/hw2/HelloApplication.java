package com.example.hw2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloApplication extends Application {
    private Image originalImage;

    @Override
    public void start(Stage stage) {

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        Button openImage = new Button("Open Image");
        openImage.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        ComboBox<String> choice = new ComboBox<>();
        choice.getItems().addAll("Grayscale", "Invert", "Blur");
        choice.setValue("Grayscale");
        choice.setStyle("-fx-background-color: #FFFFFF; -fx-font-size: 14px;");

        Button process = new Button("Process");
        process.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button save = new Button("Save");
        save.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");

        buttonBox.getChildren().addAll(openImage, choice, process, save);

        HBox imageBox = new HBox(20);
        imageBox.setAlignment(Pos.CENTER);

        VBox originalBox = new VBox(5);
        originalBox.setAlignment(Pos.CENTER);
        Label originalLabel = new Label("Original");
        originalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        ImageView original = new ImageView();
        original.setFitHeight(300);
        original.setFitWidth(250);
        original.setPreserveRatio(true);
        original.setStyle("-fx-border-color: #333333; -fx-border-width: 2px;"); 
        originalBox.getChildren().addAll(originalLabel, original);

        VBox resultBox = new VBox(5);
        resultBox.setAlignment(Pos.CENTER);
        Label resultLabel = new Label("Result");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        ImageView result = new ImageView();
        result.setFitHeight(300);
        result.setFitWidth(250);
        result.setPreserveRatio(true);
        result.setStyle("-fx-border-color: #333333; -fx-border-width: 2px;"); 
        resultBox.getChildren().addAll(resultLabel, result);

        imageBox.getChildren().addAll(originalBox, resultBox);

        root.getChildren().addAll(buttonBox, imageBox);

        openImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    originalImage = new Image(file.toURI().toString());
                    original.setImage(originalImage);
                    result.setImage(null);
                } catch (Exception ex) {
                    showErrorAlert("Error loading image", "Failed to load the image: " + ex.getMessage());
                }
            }
        });

        process.setOnAction(e -> {
            if (originalImage == null) {
                showErrorAlert("No image", "Please load an image first!");
                return;
            }
            String selectedEffect = choice.getValue();
            Image processedImage = null;
            switch (selectedEffect) {
                case "Grayscale":
                    processedImage = ProcessService.grayscale(originalImage);
                    break;
                case "Invert":
                    processedImage = ProcessService.invert(originalImage);
                    break;
                case "Blur":
                    processedImage = ProcessService.blur(originalImage);
                    break;
            }
            if (processedImage != null) {
                result.setImage(processedImage);
            } else {
                showErrorAlert("Processing error", "Failed to process the image with the selected effect!");
            }
        });
        
        save.setOnAction(e -> {
            if (result.getImage() == null) {
                showErrorAlert("No processed image", "Please process an image before saving!");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                ProcessService.saveImage(result.getImage(), file);
            }
        });

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Image Processing App");
        stage.setScene(scene);
        stage.show();
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
