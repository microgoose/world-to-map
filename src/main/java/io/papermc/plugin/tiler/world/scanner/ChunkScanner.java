package io.papermc.plugin.tiler.world.scanner;

import io.papermc.plugin.tiler.world.scanner.common.ChunkScanHandler;
import io.papermc.plugin.tiler.world.manager.WorldManager;
import io.papermc.plugin.tiler.world.mca.model.MCAChunk;

public class ChunkScanner {
    private final WorldManager world;
    private final ChunkScanHandler chunkHandler;

    public ChunkScanner(WorldManager world, ChunkScanHandler chunkHandler) {
        this.world = world;
        this.chunkHandler = chunkHandler;
    }

    public void scanChunk(int chunkX, int chunkZ) {
        MCAChunk chunk = world.loadChunk(chunkX, chunkZ);
        if (chunk == null) return;

        if ("minecraft:full".equals(chunk.status)) {
            chunkHandler.handleChunk(chunk);
        }
    }
}
