package io.papermc.plugin.tiler.map.exporter.common;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageCompressor {

    public static BufferedImage compress(BufferedImage source, int factor) {
        if (factor <= 0 || (factor & (factor - 1)) != 0) {
            throw new IllegalArgumentException("The compression ratio must be a power of two");
        }

        int newWidth = source.getWidth() / factor;
        int newHeight = source.getHeight() / factor;

        BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(source, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return compressedImage;
    }
}
