package io.papermc.plugin.tiler.world.mca.model;

import io.papermc.plugin.tiler.world.config.SectionConfig;

public class MCAChunk {
    public final int x;
    public final int z;
    public String status;
    public final MCASection[] sections;

    public MCAChunk(int x, int z, String status, MCASection[] sections) {
        this.x = x;
        this.z = z;
        this.status = status;
        this.sections = sections;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getBlockType(int x, int y, int z) {
        int sectionIndex = y / SectionConfig.BLOCKS_SIDE - SectionConfig.MIN_SECTION_Y;
        MCASection section = sections[sectionIndex];
        return section.getBlockType(x, y, z);
    }
}
