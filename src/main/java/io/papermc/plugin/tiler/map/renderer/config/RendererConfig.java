package io.papermc.plugin.tiler.map.renderer.config;

import io.papermc.plugin.tiler.world.config.ChunkConfig;

public class RendererConfig {
    public static int BLOCK_PIXEL_SIDE = 4;
    public static int CHUNK_PIXEL_SIDE = ChunkConfig.BLOCKS_SIDE * BLOCK_PIXEL_SIDE; //16 * 4 = 64
}
