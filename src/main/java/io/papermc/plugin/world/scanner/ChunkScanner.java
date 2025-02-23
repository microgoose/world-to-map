package io.papermc.plugin.world.scanner;

import io.papermc.plugin.world.manager.WorldManager;
import io.papermc.plugin.world.mca.model.MCAChunk;

public class ChunkScanner {
    private final WorldManager world;
    private final WorldStream worldStream;

    public ChunkScanner(WorldManager world, WorldStream worldStream) {
        this.world = world;
        this.worldStream = worldStream;
    }

    public void scanChunk(int chunkX, int chunkZ) {
        MCAChunk chunk = world.loadChunk(chunkX, chunkZ);
        if (chunk == null) return;

        worldStream.handleChunk(chunk);
    }
}
