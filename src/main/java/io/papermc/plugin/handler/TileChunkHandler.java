package io.papermc.plugin.handler;

import io.papermc.plugin.tiler.map.exporter.TileExporter;
import io.papermc.plugin.tiler.world.mca.model.MCAChunk;
import io.papermc.plugin.tiler.world.scanner.common.ChunkScanHandler;

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
