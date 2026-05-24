package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IntroAnimation {

    private Timeline timeline;
    private int index;

    public static List<Frame> loadFrameFromPNG(String path, int x, int y) {
        InputStream inputStream = IntroAnimation.class.getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found!");
        }

        Image image = new Image(inputStream);
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        List<Frame> frames = new ArrayList<>();
        for (int offsetY = 0; offsetY < height; offsetY++){
            for (int offsetX = 0; offsetX < width; offsetX++) {
                int argb = pixelReader.getArgb(offsetX, offsetY);
                int a = (argb >>> 24) & 0xFF;
                if (a == 0) continue;
                frames.add(new Frame(offsetX + x, offsetY + y, argb));
            }
        }

        return frames;
    }

    public void play(List<Frame> frames, int cellsPerTick, int tickMs, java.util.function.Consumer<Frame> apply, Runnable finished) {
        stop();
        index = 0;

        timeline = new Timeline(new KeyFrame(Duration.millis(tickMs), e -> {
            for (int i = 0; i < cellsPerTick && index < frames.size(); i++, index++) {
                apply.accept(frames.get(index));
            }
            if (index >= frames.size()) {
                stop();
                if (finished != null) {
                    finished.run();
                }
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
}
