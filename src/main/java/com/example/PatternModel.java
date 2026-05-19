package com.example;

public class PatternModel {
    private int width;
    private int height;
    private int[][] cells;

    public PatternModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new int[width][height];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getCells() {
        return cells;
    }

    public int getCell(int x, int y) {
        return cells[x][y];
    }

    public void setCell(int x, int y, int color) {
        cells[x][y] = color;
    }
}
