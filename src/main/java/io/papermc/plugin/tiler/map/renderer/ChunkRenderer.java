package io.papermc.plugin.tiler.map.renderer;

import io.papermc.plugin.tiler.map.renderer.common.RenderArea;
import io.papermc.plugin.tiler.map.renderer.config.RendererConfig;
import io.papermc.plugin.tiler.map.renderer.model.PixelBuffer;
import io.papermc.plugin.tiler.world.config.ChunkConfig;

public class ChunkRenderer {

    private final int minHeight;

    public ChunkRenderer(int minHeight) {
        this.minHeight = minHeight;
    }

    public PixelBuffer render(RenderArea renderArea) {
        PixelBuffer pb = new PixelBuffer(RendererConfig.CHUNK_PIXEL_SIDE, RendererConfig.CHUNK_PIXEL_SIDE);
        BlockRenderer renderer = new BlockRenderer(renderArea, pb, minHeight);

        for (int localX = 0; localX < ChunkConfig.BLOCKS_SIDE; localX++) {
            for (int localZ = 0; localZ < ChunkConfig.BLOCKS_SIDE; localZ++) {
                renderer.render(localX, renderArea.getHeight(localX, localZ), localZ);
            }
        }

        return pb;
    }
}