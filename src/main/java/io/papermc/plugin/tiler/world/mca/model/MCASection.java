package io.papermc.plugin.tiler.world.mca.model;

import io.papermc.plugin.tiler.world.mca.utils.MCAMath;

public class MCASection {
    public final byte y;
    public final String[] palette;
    private final long[] blocks;
    private final int bitsPerBlock;

    public MCASection(byte y, String[] palette, long[] blocks) {
        this.y = y;
        this.blocks = blocks;
        this.palette = palette;
        this.bitsPerBlock = (blocks == null || blocks.length == 0) ? -1 : (blocks.length * 64 / 4096);
    }

    public String getBlockType(int x, int y, int z) {
        if (blocks == null || bitsPerBlock <= 0) return palette[0];

        int blockIndex = ((y & 0xF) << 8) + ((z & 0xF) << 4) + (x & 0xF);
        long value = MCAMath.getValueFromLongArray(blocks, blockIndex, bitsPerBlock);

        return palette[(int) value];
    }
}

