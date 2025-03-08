package io.papermc.plugin.tiler.world.scanner.common;

import io.papermc.plugin.tiler.world.mca.model.MCAChunk;

public interface ChunkScanHandler {

    void handleChunk(MCAChunk chunk);
}
