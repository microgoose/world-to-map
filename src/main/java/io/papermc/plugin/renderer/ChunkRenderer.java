package io.papermc.plugin.renderer;

import io.papermc.plugin.core.config.ChunkConfig;
import io.papermc.plugin.renderer.common.RenderArea;
import io.papermc.plugin.renderer.model.PixelBuffer;

import static io.papermc.plugin.renderer.config.RendererConfig.CHUNK_PIXEL_SIDE;

public class ChunkRenderer {

    private final int minHeight;

    public ChunkRenderer(int minHeight) {
        this.minHeight = minHeight;
    }

    public PixelBuffer render(RenderArea renderArea) {
        PixelBuffer pb = new PixelBuffer(CHUNK_PIXEL_SIDE, CHUNK_PIXEL_SIDE);
        BlockRenderer renderer = new BlockRenderer(renderArea, pb, minHeight);

        for (int localX = 0; localX < ChunkConfig.BLOCKS_SIDE; localX++) {
            for (int localZ = 0; localZ < ChunkConfig.BLOCKS_SIDE; localZ++) {
                renderer.render(localX, renderArea.getHeight(localX, localZ), localZ);
            }
        }

        return pb;
    }
}