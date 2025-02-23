package io.papermc.plugin.renderer;

import io.papermc.plugin.renderer.common.RenderArea;
import io.papermc.plugin.renderer.config.RendererConfig;
import io.papermc.plugin.renderer.model.PixelBuffer;
import io.papermc.plugin.renderer.utils.IntegerColors;

public class BlockRenderer {
    private final int BLOCK_WIDTH = RendererConfig.BLOCK_PIXEL_SIDE;
    private final int BLOCK_HEIGHT = RendererConfig.BLOCK_PIXEL_SIDE;
    private final int minHeight;
    private final RenderArea area;
    private final PixelBuffer pb;

    public BlockRenderer(RenderArea area, PixelBuffer pb, int minHeight) {
        this.area = area;
        this.pb = pb;
        this.minHeight = minHeight;
    }

    public void render(int localX, int height, int localZ) {
        if (area.isWater(localX, height, localZ)) {
            renderWater(localX, height, localZ);
        } else if (area.isPlant(localX, height, localZ)) {
            renderPlant(localX, height, localZ);
        } else {
            renderBlock(localX, height, localZ);
            drawShading(localX, height, localZ);
        }
    }

    private void renderBlock(int localX, int height, int localZ) {
        int baseColor = area.getMapColor(localX, height, localZ);
        pb.fillRect(
            localX * BLOCK_WIDTH,
            localZ * BLOCK_HEIGHT,
            BLOCK_WIDTH,
            BLOCK_HEIGHT,
            baseColor
        );
    }

    private void renderPlant(int localX, int height, int localZ) {
        for (int y = height; y >= minHeight; y--) {
            if (area.isWater(localX, y, localZ))
                continue;

            if (area.isSolid(localX, y, localZ)) {
                renderBlock(localX, y, localZ);

                int plantColor = area.getMapColor(localX, y, localZ);
                int startX = localX * BLOCK_WIDTH;
                int startZ = localZ * BLOCK_HEIGHT;

                pb.set(startX + 1, startZ + 1, plantColor);

                return;
            }
        }
    }

    private void renderWater(int localX, int height, int localZ) {
        int baseColor = area.getMapColor(localX, height, localZ);

        int depth = 0;

        for (int y = height; y >= minHeight; y--) {
            if (area.isWater(localX, y, localZ))
                continue;

            if (area.isSolid(localX, y, localZ)) {
                depth = height - y;
                break;
            }
        }

        double heightDiff = (double) depth * 0.1D + (double) (localX + localZ & 1) * 0.2D;

        int brightness;
        if (heightDiff < 0.5D) {
            brightness = 0x00;
        } else if (heightDiff > 0.9D) {
            brightness = 0x44;
        } else {
            brightness = 0x22;
        }

        pb.fillRect(
            localX * BLOCK_WIDTH,
            localZ * BLOCK_HEIGHT,
            BLOCK_WIDTH,
            BLOCK_HEIGHT,
            IntegerColors.blend(brightness << 24, baseColor)
        );
    }

    private void drawShading(int localX, int height, int localZ) {
        int leftHeight = area.getHeight(localX - 1, localZ);
        int topHeight = area.getHeight(localX, localZ - 1);

        int leftDiff = height - leftHeight;
        int topDiff = height - topHeight;
        int baseColor = area.getMapColor(localX, height, localZ);

        if (leftDiff > 0) {
            int shade = IntegerColors.adjustBrightness(baseColor, 20);
            pb.fillRect(localX * BLOCK_WIDTH, localZ * BLOCK_HEIGHT, 1, BLOCK_HEIGHT, shade);
        } else if (leftDiff < 0) {
            int shade = IntegerColors.adjustBrightness(baseColor, -20);
            pb.fillRect(localX * BLOCK_WIDTH, localZ * BLOCK_HEIGHT, 1, BLOCK_HEIGHT, shade);
        }

        if (topDiff > 0) {
            int shade = IntegerColors.adjustBrightness(baseColor, 20);
            pb.fillRect(localX * BLOCK_WIDTH, localZ * BLOCK_HEIGHT, BLOCK_WIDTH, 1, shade);
        } else if (topDiff < 0) {
            int shade = IntegerColors.adjustBrightness(baseColor, -20);
            pb.fillRect(localX * BLOCK_WIDTH, localZ * BLOCK_HEIGHT, BLOCK_WIDTH, 1, shade);
        }
    }
}
