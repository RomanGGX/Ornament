package com.example;

public class PatternModel {
    private int width;
    private int height;
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
}
