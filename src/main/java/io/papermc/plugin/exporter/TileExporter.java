package io.papermc.plugin.exporter;

import io.papermc.plugin.exporter.common.ImageCompressor;
import io.papermc.plugin.exporter.config.TileConfig;
import io.papermc.plugin.exporter.model.ChunkRenderArea;
import io.papermc.plugin.exporter.repository.TileRepository;
import io.papermc.plugin.renderer.ChunkRenderer;
import io.papermc.plugin.renderer.model.PixelBuffer;
import io.papermc.plugin.world.manager.WorldManager;
import io.papermc.plugin.world.mca.model.MCAChunk;
import io.papermc.plugin.world.scanner.WorldStream;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.papermc.plugin.exporter.config.TileConfig.TILE_SIDE;
import static io.papermc.plugin.renderer.config.RendererConfig.CHUNK_PIXEL_SIDE;

public class TileExporter implements WorldStream {
    private final ChunkRenderer chunkRenderer;
    private final TileRepository tileRepository;
    private final WorldManager worldManager;

    public TileExporter(ChunkRenderer chunkRenderer, TileRepository tileRepository, WorldManager worldManager) {
        this.chunkRenderer = chunkRenderer;
        this.tileRepository = tileRepository;
        this.worldManager = worldManager;
    }

    @Override
    public void handleChunk(MCAChunk chunk) {
        processChunk(chunk);
    }

    @Override
    public void afterRegion(int regionX, int regionY) {
        //todo is not multi-threading
        //todo if zoom (0-3 lvls) changes, it will slow down plugin
        exportAll();
    }

    public void processChunk(MCAChunk chunk) {
        MCAChunk leftChunk = worldManager.loadChunk(chunk.getX() - 1, chunk.getZ());
        MCAChunk topChunk = worldManager.loadChunk(chunk.getX(), chunk.getZ() - 1);
        ChunkRenderArea renderArea = new ChunkRenderArea(chunk, leftChunk, topChunk);

        PixelBuffer pb = chunkRenderer.render(renderArea);
        BufferedImage chunkImage = toChunkImage(pb);
        BufferedImage scaledChunkImage = chunkImage;

        for (int zoom = 0; zoom <= TileConfig.MAX_ZOOM_DISTANT; zoom++) {
            int compressionFactor = (1 << zoom);
            int scaledChunkSide = Math.floorDiv(CHUNK_PIXEL_SIDE, compressionFactor);
            int chunkPerTile = TILE_SIDE / scaledChunkSide;

            int tX = Math.floorDiv(chunk.getX(), chunkPerTile);
            int tY = Math.floorDiv(chunk.getZ(), chunkPerTile);
            int localX = Math.floorMod(chunk.getX(), chunkPerTile);
            int localY = Math.floorMod(chunk.getZ(), chunkPerTile);

            BufferedImage tileImage = tileRepository.getTile(compressionFactor, tX, tY);

            int offsetX = localX * scaledChunkSide;
            int offsetY = localY * scaledChunkSide;

            scaledChunkImage = (zoom == 0) ? chunkImage : ImageCompressor.compress(scaledChunkImage, 2);

            Graphics2D g2d = tileImage.createGraphics();
            g2d.drawImage(scaledChunkImage, offsetX, offsetY, null);
            g2d.dispose();

            tileRepository.updateTile(compressionFactor, tX, tY, tileImage);
        }
    }

    public void exportAll() {
        tileRepository.release();
    }

    private BufferedImage toChunkImage(PixelBuffer pb) {
        BufferedImage chunkImage = new BufferedImage(CHUNK_PIXEL_SIDE, CHUNK_PIXEL_SIDE, BufferedImage.TYPE_INT_ARGB);
        int[] chunkPixels = ((java.awt.image.DataBufferInt) chunkImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(pb.getPixels(), 0, chunkPixels, 0, chunkPixels.length);
        return chunkImage;
    }
}
