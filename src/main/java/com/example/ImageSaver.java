package com.example;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class ImageSaver {

    // Exports model to a PNG file
    public static void saveImage(PatternModel model, File file) throws IOException{
        int width = model.getWidth();
        int height = model.getHeight();

        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setArgb(x, y, model.getCellColor(x, y));
            }
        }

        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }

    // Loads PNG pixels into the model
    public static void loadImage(PatternModel model, Image image) throws IOException {
        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalArgumentException("Cannot read image file");
        }

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        if (width != model.getWidth() || height != model.getHeight()) {
            throw new IllegalArgumentException("Image size " + width + "x" + height +
                    " must match grid " + model.getWidth() + "x" + model.getHeight());
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                model.setCellColor(x, y, pixelReader.getArgb(x, y));
            }
        }
    }
}
