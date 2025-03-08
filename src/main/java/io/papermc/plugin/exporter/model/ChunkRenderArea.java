package io.papermc.plugin.exporter.model;

import io.papermc.plugin.core.config.ChunkConfig;
import io.papermc.plugin.core.config.SectionConfig;
import io.papermc.plugin.core.model.BlockState;
import io.papermc.plugin.core.registries.BlockNamesRegistry;
import io.papermc.plugin.renderer.common.RenderArea;
import io.papermc.plugin.world.mca.model.MCAChunk;
import io.papermc.plugin.world.mca.model.MCASection;

public class ChunkRenderArea implements RenderArea {
    private final int[] heightmap;
    private final MCAChunk chunk;
    private final MCAChunk leftChunk;
    private final MCAChunk topChunk;
    private final BlockState blockState;

    public ChunkRenderArea(MCAChunk chunk, MCAChunk leftChunk, MCAChunk topChunk) {
        this.heightmap = new int[ChunkConfig.BLOCKS_COUNT + 2 * ChunkConfig.BLOCKS_SIDE + 1]; //17x17 (from x-1, z-1)
        this.chunk = chunk;
        this.leftChunk = leftChunk;
        this.topChunk = topChunk;
        this.blockState = new BlockState();
        buildHeightmap();
    }

    @Override
    public int getHeight(int localX, int localZ) {
        int index = (localX + 1) + (localZ + 1) * 17;
        return heightmap[index];
    }

    @Override
    public int getMapColor(int localX, int y, int localZ) {
        return blockState.getMapColor(getBlockType(localX, y, localZ));
    }

    @Override
    public boolean isWater(int localX, int y, int localZ) {
        return blockState.isWater(getBlockType(localX, y, localZ));
    }

    @Override
    public boolean isPlant(int localX, int y, int localZ) {
        return blockState.isPlant(getBlockType(localX, y, localZ));
    }

    @Override
    public boolean isSolid(int localX, int y, int localZ) {
        return blockState.isSolid(getBlockType(localX, y, localZ));
    }

    private String getBlockType(int localX, int y, int localZ) {
        if (y == Integer.MIN_VALUE)
            return BlockNamesRegistry.AIR.getName();

        return getChunk(localX, localZ).getBlockType(localX, y, localZ);
    }

    private MCAChunk getChunk(int localX, int localZ) {
        if (localX < 0)
            return leftChunk;
        if (localZ < 0)
            return topChunk;

        return chunk;
    }

    private void buildHeightmap() {
        for (int z = 0; z < ChunkConfig.BLOCKS_SIDE; z++) {
            int y = getSolidBlockY(leftChunk, ChunkConfig.BLOCKS_SIDE - 1, z);
            int index = (z + 1) * 17;
            heightmap[index] = y;
        }

        for (int x = 0; x < ChunkConfig.BLOCKS_SIDE; x++) {
            int y = getSolidBlockY(topChunk, x, ChunkConfig.BLOCKS_SIDE - 1);
            int index = x + 1;
            heightmap[index] = y;
        }

        for (int x = 0; x < ChunkConfig.BLOCKS_SIDE; x++) {
            for (int z = 0; z < ChunkConfig.BLOCKS_SIDE; z++) {
                int y = getSolidBlockY(chunk, x, z);
                int index = (x + 1) + (z + 1) * 17;
                heightmap[index] = y;
            }
        }
    }

    private int getSolidBlockY(MCAChunk chunk, int x, int z) {
        if (chunk == null) return Integer.MIN_VALUE; //todo nether world?

        for (int i = chunk.sections.length - 1; i >= 0; i--) {
            MCASection section = chunk.sections[i];

            if (section.palette.length == 1 && blockState.isTransparent(section.palette[0]))
                continue;

            int minY = SectionConfig.BLOCKS_SIDE * section.y;
            int maxY = minY + SectionConfig.BLOCKS_SIDE; //from top to bottom

            for (int y = maxY; y > minY; y--) {
                String blockType = chunk.getBlockType(x, y, z);

                if (!blockState.isTransparent(blockType))
                    return y;
            }
        }

        return Integer.MIN_VALUE;
    }
}