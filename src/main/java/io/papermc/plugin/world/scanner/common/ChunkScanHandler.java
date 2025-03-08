package io.papermc.plugin.world.scanner.common;

import io.papermc.plugin.world.mca.model.MCAChunk;

public interface ChunkScanHandler {

    void handleChunk(MCAChunk chunk);
}
