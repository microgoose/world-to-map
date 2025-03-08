package io.papermc.plugin.command.handler;

import io.papermc.plugin.exporter.TileExporter;
import io.papermc.plugin.world.mca.model.MCAChunk;
import io.papermc.plugin.world.scanner.common.ChunkScanHandler;

public class TileChunkHandler implements ChunkScanHandler {

    private final TileExporter tileExporter;

    public TileChunkHandler(TileExporter tileExporter) {
        this.tileExporter = tileExporter;
    }

    @Override
    public void handleChunk(MCAChunk chunk) {
        tileExporter.processChunk(chunk);
    }
}
