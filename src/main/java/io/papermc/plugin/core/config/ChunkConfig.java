package io.papermc.plugin.core.config;

public class ChunkConfig {
    public static final int BLOCKS_SIDE = 16;
    public static final int BLOCKS_COUNT = BLOCKS_SIDE * BLOCKS_SIDE;
    public static final int SECTIONS_COUNT = SectionConfig.MAX_SECTION_Y - SectionConfig.MIN_SECTION_Y + 1;
}
