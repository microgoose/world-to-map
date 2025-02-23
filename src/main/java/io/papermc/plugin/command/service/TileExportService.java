package io.papermc.plugin.command.service;

import io.papermc.plugin.exporter.TileExporter;
import io.papermc.plugin.exporter.repository.TileRepository;
import io.papermc.plugin.renderer.ChunkRenderer;
import io.papermc.plugin.world.manager.WorldManager;
import io.papermc.plugin.world.scanner.ChunkScanner;
import io.papermc.plugin.world.scanner.RegionScanner;
import io.papermc.plugin.world.scanner.WorldScanner;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

public class TileExportService {

    private final Logger logger;
    private final JavaPlugin plugin;

    public TileExportService(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
        this.plugin = plugin;
    }

    public void generateRegionMap(World world, int regionX, int regionZ) {
        logger.info("Generating region tiles [" + regionX + ", " + regionZ + "] in " + world.getName() + "...");

        WorldManager worldManager = new WorldManager(getWorldFolder(world));
        worldManager.loadRegion(regionX, regionZ);

        TileExporter tileExporter = buildTileExporter(world, worldManager);
        ChunkScanner chunkScanner = new ChunkScanner(worldManager, tileExporter);
        RegionScanner regionScanner = new RegionScanner(chunkScanner);

        regionScanner.scanRegion(regionX, regionZ);
        tileExporter.exportAll();

        logger.info("Generating region tiles [" + regionX + ", " + regionZ + "] in " + world.getName() + " done.");
    }

    public void generateChunkMap(World world, int chunkX, int chunkZ) {
        logger.info("Generating chunk tiles [" + chunkX + ", " + chunkZ + "] in " + world.getName() + "...");

        WorldManager worldManager = new WorldManager(getWorldFolder(world));
        TileRepository tileRepository = new TileRepository(getOutputFolder(world));
        ChunkRenderer chunkRenderer = new ChunkRenderer(world.getMinHeight());
        TileExporter tileExporter = new TileExporter(chunkRenderer, tileRepository, worldManager);

        ChunkScanner chunkScanner = new ChunkScanner(worldManager, tileExporter);
        chunkScanner.scanChunk(chunkX, chunkZ);
        tileExporter.exportAll();

        logger.info("Generating chunk tiles [" + chunkX + ", " + chunkZ + "] in " + world.getName() + " done.");
    }

    public void generateWorldMap(World world) {
        logger.info("Generating tiles in " + world.getName() + "...");

        WorldManager worldManager = new WorldManager(getWorldFolder(world));
        TileExporter tileExporter = buildTileExporter(world, worldManager);
        WorldScanner worldScanner = new WorldScanner(logger, getWorldFolder(world), worldManager, tileExporter);
        worldScanner.handleWorld();

        logger.info("Generating tiles in " + world.getName() + " done.");
    }

    private TileExporter buildTileExporter(World world, WorldManager worldManager) {
        TileRepository tileRepository = new TileRepository(getOutputFolder(world));
        ChunkRenderer chunkRenderer = new ChunkRenderer(world.getMinHeight());
        return new TileExporter(chunkRenderer, tileRepository, worldManager);
    }

    private Path getOutputFolder(World world) {
        File outputFolder = new File(plugin.getDataFolder(), world.getName());
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        return outputFolder.toPath();
    }

    private Path getWorldFolder(World world) {
        return world.getWorldFolder().toPath();
    }
}
