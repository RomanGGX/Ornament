package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class App extends Application {

    int width = 30;
    int height = 20;
    private final int cellSize = 15;
    private int selectedColor = 1;
    PatternModel model = new PatternModel(width, height);
    ColorPalette palette = new ColorPalette();
    Canvas canvas = new Canvas(width * cellSize, height * cellSize);
    GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

    @Override
    public void start(Stage stage) {
        graphicsContext2D.setFill(Color.valueOf("#FAEFD4"));
        graphicsContext2D.setStroke(Color.valueOf("#000000"));
        for (int y = 0; y<=height; y += 1) {
            for (int x = 0; x<=width; x += 1) {
                graphicsContext2D.fillRect(x*cellSize,y*cellSize,cellSize,cellSize);
                graphicsContext2D.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
            }
        }
        VBox vbox = new VBox(canvas);
        Scene scene = new Scene(vbox);

        stage.setTitle("Орнамент");
        stage.setScene(scene);
        stage.show();
        stage.setWidth(1000);
        stage.setHeight(500);

        canvas.setOnMousePressed(e -> paintCell(e.getX(), e.getY(), e.getButton()));
    }

    private void paintCell(double mouseX, double mouseY, MouseButton button){
        int x = (int) (mouseX / cellSize);
        int y = (int) (mouseY / cellSize);

        System.out.println("painted at:" + x + " and " + y);

        if (button == MouseButton.PRIMARY) {
            model.setCell(x, y, selectedColor);
        }
        else if (button == MouseButton.SECONDARY) {
            model.setCell(x, y, 0);
        }
        else {
            return;
        }

        renderCell(graphicsContext2D, x, y);
        System.out.println(Arrays.deepToString(model.getCells()));
    }

    private void renderCell(GraphicsContext gc, int x, int y) {
        gc.setFill(palette.getColor(model.getCell(x, y)));
        gc.fillRect(x*cellSize,y*cellSize,cellSize,cellSize);

        gc.setStroke(Color.valueOf("#000000"));
        gc.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
    }

    public static void main(String[] args) {
        launch();
    }
}