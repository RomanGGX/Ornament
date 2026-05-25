package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class App extends Application {

    int width = 21;
    int height = 21;
    private final int cellSize = 15;
    private Color selectedColorFX = Color.valueOf("#000000");
    private int selectedColorARGB = toArgb(selectedColorFX);
    PatternModel model = new PatternModel(width, height);
    private PatternModel viewModel = model;
    Canvas canvas = new Canvas(width * cellSize, height * cellSize);
    GraphicsContext graphicsContext2D   = canvas.getGraphicsContext2D();
    FileChooser fileChooser = new FileChooser();

    Button saveButton = new Button("Зберегти PNG");
    Button loadButton = new Button("Відкрити PNG");
    Button clearButton = new Button("Очистити полотно");
    CheckBox horizontalButton = new CheckBox("Горизонтальна симетрія");
    CheckBox verticalButton = new CheckBox("Вертикальна симетрія");
    CheckBox fullSymmetryButton = new CheckBox("Повна симетрія");
    Button duplicateVertically = new Button("Дублювати по вертикалі");
    Button duplicateHorizontally = new Button("Дублювати по горизонталі");
    ToggleButton previewButton = new ToggleButton("Режим дублювання");
    ColorPicker colorPicker = new ColorPicker();
    Control[] controls = {saveButton, loadButton, clearButton, horizontalButton, colorPicker,
            previewButton, horizontalButton, verticalButton, fullSymmetryButton};

    boolean horizontalMode = false;
    boolean verticalMode = false;
    boolean fullSymmetry = false;
    private boolean previewMode = false;

    @Override
    public void start(Stage stage) {
        graphicsContext2D.setFill(Color.TRANSPARENT);
        graphicsContext2D.setStroke(Color.valueOf("#000000"));
        for (int y = 0; y<=height; y += 1) {
            for (int x = 0; x<=width; x += 1) {
                graphicsContext2D.fillRect(x*cellSize,y*cellSize,cellSize,cellSize);
                graphicsContext2D.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
            }
        }

        colorPicker.setValue(Color.valueOf("#000000"));
        duplicateHorizontally.setDisable(true);
        duplicateVertically.setDisable(true);

        previewButton.setOnAction(e -> {
            previewMode = previewButton.isSelected();
            viewModel = model;

            colorPicker.setDisable(previewMode);
            clearButton.setDisable(previewMode);
            horizontalButton.setDisable(previewMode);
            verticalButton.setDisable(previewMode);
            fullSymmetryButton.setDisable(previewMode);
            duplicateVertically.setDisable(!previewMode);
            duplicateHorizontally.setDisable(!previewMode);
            saveButton.setDisable(previewMode);
            loadButton.setDisable(previewMode);

            renderAllCells();
        });

        duplicateVertically.setOnAction(e -> {
            if (!previewMode) return;
            viewModel = viewModel.duplicateVertically();
            renderAllCells();
        });

        duplicateHorizontally.setOnAction(e -> {
            if (!previewMode) return;
            viewModel = viewModel.duplicateHorizontally();
            renderAllCells();
        });

        fullSymmetryButton.setOnAction(e -> {
           if (!fullSymmetry) {
               fullSymmetry = true;
           } else {
               fullSymmetry = false;
           }
        });

        horizontalButton.setOnAction(e -> {
            if (!horizontalMode) {
                horizontalMode = true;
            } else {
                horizontalMode = false;
            }
        });

        verticalButton.setOnAction(e -> {
            if (!verticalMode) {
                verticalMode = true;
            } else {
                verticalMode = false;
            }
        });

        //Clear canvas
        clearButton.setOnAction(e -> {
            clearAllCells();
        });

        // Saving and loading png
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        saveButton.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) return;
            try {
                ImageSaver.saveImage(model, file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        loadButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file == null) return;
            clearAllCells();
            try {
                Image image = new Image(file.toURI().toString());
                ImageSaver.loadImage(model, image);

                renderAllCells();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox vbox = new VBox(canvas, colorPicker, saveButton, loadButton, clearButton, horizontalButton,
                verticalButton, fullSymmetryButton, duplicateVertically, duplicateHorizontally, previewButton);
        Scene scene = new Scene(vbox);
        vbox.setAlignment(Pos.TOP_CENTER);

        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setTitle("Орнамент (Гвоздік Роман)");
        stage.setScene(scene);
        stage.show();
        disableUI(true);
        introAnimation();

        colorPicker.setOnAction(e -> {
            selectedColorFX = colorPicker.getValue();
            selectedColorARGB = toArgb(selectedColorFX);
        });

        canvas.setOnMousePressed(e -> paintCell(e.getX(), e.getY(), e.getButton()));
    }

    private void paintCell(double mouseX, double mouseY, MouseButton button){
        if (previewMode) return;
        int x = (int) (mouseX / cellSize);
        int y = (int) (mouseY / cellSize);

        if (button == MouseButton.PRIMARY) {
            model.setCellColor(x, y, selectedColorARGB);
            if (horizontalMode) {
                model.setCellColor(x, 10 - (y - 10), selectedColorARGB);
            }
            if (verticalMode) {
                model.setCellColor(10 - (x - 10), y, selectedColorARGB);
            }
            if (fullSymmetry) {
                model.setCellColor(x, 10 - (y - 10), selectedColorARGB);
                model.setCellColor(10 - (x - 10), y, selectedColorARGB);
                model.setCellColor(10 - (x - 10), 10 - (y - 10), selectedColorARGB);
            }
        }
        else if (button == MouseButton.SECONDARY) {
            clearCell(x, y);
            if (horizontalMode) {
                clearCell(x, 10 - (y - 10));
            }
            if (verticalMode) {
                clearCell(10 - (x - 10), y);
            }
            if (fullSymmetry) {
                clearCell(x, 10 - (y - 10));
                clearCell(10 - (x - 10), y);
                clearCell(10 - (x - 10), 10 - (y - 10));
            }
        }
        else {
            return;
        }

        renderAllCells();
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

    private void renderAllCells() {
        graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // поля прозорі

        double cell = cell();
        double ox = offsetX();
        double oy = offsetY();

        for (int y = 0; y < viewModel.getHeight(); y++) {
            for (int x = 0; x < viewModel.getWidth(); x++) {
                int argb = viewModel.getCellColor(x, y);
                int a = (argb >>> 24) & 0xFF;
                if (a == 0) continue;

                graphicsContext2D.setFill(fromArgb(argb));
                graphicsContext2D.fillRect(ox + x * cell, oy + y * cell, cell, cell);
            }
        }

        graphicsContext2D.setStroke(Color.rgb(0, 0, 0, 0.35));
        double fieldW = viewModel.getWidth() * cell;
        double fieldH = viewModel.getHeight() * cell;

        for (int x = 0; x <= viewModel.getWidth(); x++) {
            double px = ox + x * cell;
            graphicsContext2D.strokeLine(px, oy, px, oy + fieldH);
        }
        for (int y = 0; y <= viewModel.getHeight(); y++) {
            double py = oy + y * cell;
            graphicsContext2D.strokeLine(ox, py, ox + fieldW, py);
        }
    }

    private void clearAllCells() {
        for (int y = 0; y<height; y += 1) {
            for (int x = 0; x<width; x += 1) {
                model.setCellColor(x, y, 0);
                graphicsContext2D.clearRect(x*cellSize,y*cellSize,cellSize,cellSize);
                graphicsContext2D.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
            }
        }
    }

    private void clearCell(int x, int y) {
        model.setCellColor(x, y, 0);
        graphicsContext2D.clearRect(x*cellSize,y*cellSize,cellSize,cellSize);
        graphicsContext2D.strokeRect(x*cellSize,y*cellSize,cellSize,cellSize);
    }

    private void introAnimation() {
        String path = "/roman.png";
        int offsetX = 0;
        int offsetY = 0;

        var frames = IntroAnimation.loadFrameFromPNG(path, offsetX, offsetY);

        IntroAnimation animator = new IntroAnimation();
        animator.play(frames, 1, 25, frame -> {
            model.setCellColor(frame.x(), frame.y(), frame.argb());
            renderAllCells();
        }, () -> disableUI(false)
        );
    }

    private void disableUI(boolean disabled) {
        for (Control c : controls) {
            c.setDisable(disabled);
        }
    }

    private double cell() {
        return Math.min(canvas.getWidth() / viewModel.getWidth(),
                canvas.getHeight() / viewModel.getHeight());
    }

    private double offsetX() {
        return (canvas.getWidth() - viewModel.getWidth() * cell()) / 2.0;
    }

    private double offsetY() {
        return (canvas.getHeight() - viewModel.getHeight() * cell()) / 2.0;
    }

    public static void main(String[] args) {
        launch();
    }
}