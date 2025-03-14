package io.papermc.plugin.tiler.world.mca.model;

import io.papermc.plugin.tiler.world.config.RegionConfig;

public class MCARegion {
    public final int x;
    public final int z;
    public final int[] offsets;
    public final MCAChunk[] chunks = new MCAChunk[RegionConfig.CHUNK_SIDE * RegionConfig.CHUNK_SIDE];

    public MCARegion(int x, int z, int[] offsets) {
        this.x = x;
        this.z = z;
        this.offsets = offsets;
    }

    public void setChunk(int chunkX, int chunkZ, MCAChunk chunk) {
        chunks[getChunkIndex(chunkX, chunkZ)] = chunk;
    }

    public MCAChunk getChunk(int chunkX, int chunkZ) {
        return chunks[getChunkIndex(chunkX, chunkZ)];
    }

    private static int getChunkIndex(int chunkX, int chunkZ) {
        return (chunkX & 31) + (chunkZ & 31) * 32;
    }
}