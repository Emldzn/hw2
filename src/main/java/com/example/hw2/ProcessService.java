package com.example.hw2;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProcessService {
    public static Image grayscale(Image a) {
        if (a == null) return null;

        int width = (int) a.getWidth();
        int height = (int) a.getHeight();

        PixelReader reader = a.getPixelReader();
        Pixels[][] pixels = new Pixels[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                pixels[y][x] = new Pixels(color);
            }
        }

        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixels[y][x].getColor();
                double grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                Color grayColor = new Color(grayValue, grayValue, grayValue, color.getOpacity());
                writer.setColor(x, y, grayColor);
            }
        }

        return result;
    }

    public static Image invert(Image a) {
        if (a == null) return null;

        int width = (int) a.getWidth();
        int height = (int) a.getHeight();

        PixelReader reader = a.getPixelReader();
        Pixels[][] pixels = new Pixels[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                pixels[y][x] = new Pixels(color);
            }
        }

        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixels[y][x].getColor();
                Color invertedColor = new Color(
                        1.0 - color.getRed(),
                        1.0 - color.getGreen(),
                        1.0 - color.getBlue(),
                        color.getOpacity()
                );
                writer.setColor(x, y, invertedColor);
            }
        }

        return result;
    }

    public static Image blur(Image a) {
        if (a == null) return null;

        int width = (int) a.getWidth();
        int height = (int) a.getHeight();

        PixelReader reader = a.getPixelReader();
        Pixels[][] pixels = new Pixels[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                pixels[y][x] = new Pixels(color);
            }
        }

        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        int blurRadius = 2;
        for (int y = blurRadius; y < height - blurRadius; y++) {
            for (int x = blurRadius; x < width - blurRadius; x++) {
                double redSum = 0, greenSum = 0, blueSum = 0;
                int count = 0;
                for (int dy = -blurRadius; dy <= blurRadius; dy++) {
                    for (int dx = -blurRadius; dx <= blurRadius; dx++) {
                        Color color = pixels[y + dy][x + dx].getColor();
                        redSum += color.getRed();
                        greenSum += color.getGreen();
                        blueSum += color.getBlue();
                        count++;
                    }
                }
                Color blurredColor = new Color(
                        redSum / count,
                        greenSum / count,
                        blueSum / count,
                        pixels[y][x].getColor().getOpacity()
                );
                writer.setColor(x, y, blurredColor);
            }
        }

        return result;
    }

    public static void saveImage(Image image, File file) {
        if (image == null || file == null) return;

        try {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            PixelReader reader = image.getPixelReader();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = reader.getColor(x, y);
                    int r = (int) (color.getRed() * 255);
                    int g = (int) (color.getGreen() * 255);
                    int b = (int) (color.getBlue() * 255);
                    int a = (int) (color.getOpacity() * 255);
                    int argb = (a << 24) | (r << 16) | (g << 8) | b;
                    bufferedImage.setRGB(x, y, argb);
                }
            }

            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }
}