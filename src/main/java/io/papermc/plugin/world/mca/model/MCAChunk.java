package io.papermc.plugin.world.mca.model;

import io.papermc.plugin.core.config.SectionConfig;

public class MCAChunk {
    public final int x;
    public final int z;
    public final MCASection[] sections;

    public MCAChunk(int x, int z, MCASection[] sections) {
        this.x = x;
        this.z = z;
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
