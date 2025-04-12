package com.example.hw2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloApplication extends Application {
    private Image originalImage;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        HBox h1 = new HBox(10);
        HBox h2 = new HBox(20);
        root.getChildren().addAll(h1, h2);

        Button openImage = new Button("Open Image");
        ComboBox<String> choice = new ComboBox<>();
        choice.getItems().addAll("Grayscale", "Invert", "Blur");
        choice.setValue("Grayscale");
        Button process = new Button("Process");
        Button save = new Button("Save");
        h1.getChildren().addAll(openImage, choice, process, save);

        VBox originalBox = new VBox(5);
        Label originalLabel = new Label("Original");
        ImageView original = new ImageView();
        original.setFitHeight(300);
        original.setFitWidth(250);
        original.setPreserveRatio(true);
        originalBox.getChildren().addAll(originalLabel, original);

        VBox resultBox = new VBox(5);
        Label resultLabel = new Label("Result");
        ImageView result = new ImageView();
        result.setFitHeight(300);
        result.setFitWidth(250);
        result.setPreserveRatio(true);
        resultBox.getChildren().addAll(resultLabel, result);

        h2.getChildren().addAll(originalBox, resultBox);

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
                    System.err.println("Error loading image: " + ex.getMessage());
                }
            }
        });

        process.setOnAction(e -> {
            if (originalImage == null) {
                System.out.println("No image loaded!");
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
                System.out.println("Failed to process image!");
            }
        });

        save.setOnAction(e -> {
            if (result.getImage() == null) {
                System.out.println("No processed image to save!");
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

    public static void main(String[] args) {
        launch();
    }
}