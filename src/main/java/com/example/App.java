package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class App extends Application {

    int width = 20;
    int height = 20;
    private final int cellSize = 15;
    private Color selectedColorFX = Color.valueOf("#000000");
    private int selectedColorARGB = toArgb(selectedColorFX);
    PatternModel model = new PatternModel(width, height);
    ColorPalette palette = new ColorPalette();
    Canvas canvas = new Canvas(width * cellSize, height * cellSize);
    GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
    ColorPaletteUI colorUI = new ColorPaletteUI();

    @Override
    public void start(Stage stage) {
        graphicsContext2D.setFill(Color.valueOf("#FFFFFF"));
        graphicsContext2D.setStroke(Color.valueOf("#000000"));
        for (int y = 0; y<=height; y += 1) {
            for (int x = 0; x<=width; x += 1) {
                graphicsContext2D.fillRect(x*cellSize,y*cellSize,cellSize,cellSize);
                graphicsContext2D.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
            }
        }

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.valueOf("#000000"));
        VBox palette = new VBox(colorPicker);

        VBox vbox = new VBox(canvas, colorPicker);
        Scene scene = new Scene(vbox);
        vbox.setAlignment(Pos.TOP_CENTER);

        stage.setWidth(1000);
        stage.setHeight(500);
        stage.setTitle("Орнамент");
        stage.setScene(scene);

        stage.show();

        colorPicker.setOnAction(e -> {
            selectedColorFX = colorPicker.getValue();
            selectedColorARGB = toArgb(selectedColorFX);
        });

        canvas.setOnMousePressed(e -> paintCell(e.getX(), e.getY(), e.getButton()));
    }

    private void paintCell(double mouseX, double mouseY, MouseButton button){
        int x = (int) (mouseX / cellSize);
        int y = (int) (mouseY / cellSize);

        System.out.println("painted at:" + x + " and " + y);

        if (button == MouseButton.PRIMARY) {
            model.setCellColor(x, y, selectedColorARGB);
        }
        else if (button == MouseButton.SECONDARY) {
            model.setCellColor(x, y, -1);
        }
        else {
            return;
        }

        renderCell(graphicsContext2D, x, y);
        System.out.println(Arrays.deepToString(model.getCells()));
    }

    private void renderCell(GraphicsContext gc, int x, int y) {
        gc.setFill(fromArgb(model.getCellColor(x, y)));
        gc.fillRect(x*cellSize,y*cellSize,cellSize,cellSize);

        gc.setStroke(Color.valueOf("#000000"));
        gc.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
    }

    private int toArgb(Color c) {
        int a = (int)Math.round(c.getOpacity() * 255);
        int r = (int)Math.round(c.getRed() * 255);
        int g = (int)Math.round(c.getGreen() * 255);
        int b = (int)Math.round(c.getBlue() * 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private Color fromArgb(int argb) {
        int a = (argb >>> 24) & 0xFF;
        int r = (argb >>> 16) & 0xFF;
        int g = (argb >>> 8) & 0xFF;
        int b = (argb) & 0xFF;
        return Color.rgb(r, g, b, a / 255.0);
    }

    public static void main(String[] args) {
        launch();
    }
}