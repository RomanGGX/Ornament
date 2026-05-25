package com.example;

public class PatternModel {
    private final int width;
    private final int height;
    private int[][] cellColor;

    public PatternModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.cellColor = new int[width][height];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getCells() {
        return cellColor;
    }

    public int getCellColor(int x, int y) {
        return cellColor[x][y];
    }

    public void setCellColor(int x, int y, int colorARGB) {
        cellColor[x][y] = colorARGB;
    }

    // Creates a new model duplicated vertically
    public PatternModel duplicateVertically() {
        PatternModel model = new PatternModel(width, height * 2);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = getCellColor(x, y);
                model.setCellColor(x, y, color);
                model.setCellColor(x, y + height, color);
            }
        }
        return model;
    }

    // Creates a new model duplicated horizontally
    public PatternModel duplicateHorizontally() {
        PatternModel model = new PatternModel(width * 2, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = getCellColor(x, y);
                model.setCellColor(x, y, color);
                model.setCellColor(x + width, y, color);
            }
        }
        return model;
    }
}
