package com.example;

import javafx.scene.paint.Color;

public class ColorPalette {
    Color[] palette = {
            Color.valueOf("#FAEFD4"),
            Color.valueOf("#000000"),
    };

    public Color getColor(int index) {
        return palette[index];
    }
}
