package io.papermc.plugin.service;

import io.papermc.plugin.model.ChangedChunks;
import io.papermc.plugin.model.ChunkCoordinate;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.logging.Logger;

public class TileUpdateService {
    private final Logger logger;
    private final TileExportService tileExportService;
    private final ChangedChunks chunks;

    public TileUpdateService(TileExportService service, JavaPlugin plugin, ChangedChunks chunks) {
        this.logger = plugin.getLogger();
        this.tileExportService = service;
        this.chunks = chunks;
    }

    public void updateTiles() {
        logger.info("Updating chunk tiles...");

        Set<ChunkCoordinate> chunkCoordinates = chunks.getAndClear();

        for (ChunkCoordinate chunkCoordinate : chunkCoordinates) {
            tileExportService.generateChunkMap(
                chunkCoordinate.getWorld(),
                chunkCoordinate.getX(),
                chunkCoordinate.getZ()
            );
        }

        logger.info("Updating chunk tiles done.");
    }
}
