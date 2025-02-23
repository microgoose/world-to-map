package io.papermc.plugin.renderer.model;

public class PixelBuffer {
    private final int[] pixels;
    private final int width;
    private final int height;

    public PixelBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixel(int x, int y) {
        return pixels[y * width + x];
    }

    public int[] getPixels() {
        return pixels;
    }

    public void set(int x, int y, int color) {
        pixels[y * width + x] = color;
    }

    public void fillRect(int x, int y, int w, int h, int color) {
        for (int dy = 0; dy < h; dy++) {
            int rowStart = (y + dy) * width + x;
            for (int dx = 0; dx < w; dx++) {
                pixels[rowStart + dx] = color;
            }
        }
    }

    public void drawRect(int x, int y, int w, int h, int color) {
        for (int dx = 0; dx < w; dx++) {
            set(x + dx, y, color);
            set(x + dx, y + h - 1, color);
        }

        for (int dy = 0; dy < h; dy++) {
            set(x, y + dy, color);
            set(x + w - 1, y + dy, color);
        }
    }
}
