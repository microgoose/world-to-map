package io.papermc.plugin.renderer.config;

import io.papermc.plugin.core.config.ChunkConfig;

public class RendererConfig {
    public static int BLOCK_PIXEL_SIDE = 4;
    public static int CHUNK_PIXEL_SIDE = ChunkConfig.BLOCKS_SIDE * BLOCK_PIXEL_SIDE; //16 * 4 = 64
}
