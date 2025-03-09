package io.papermc.plugin;

import io.papermc.plugin.command.ExportChunkTileCommand;
import io.papermc.plugin.command.ExportRegionTileCommand;
import io.papermc.plugin.command.ExportWorldTileCommand;
import io.papermc.plugin.listener.ChunksChangesListener;
import io.papermc.plugin.model.ChangedChunks;
import io.papermc.plugin.service.TileExportService;
import io.papermc.plugin.service.TileUpdateService;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class WorldToMap extends JavaPlugin {

    private ChangedChunks changedChunks;
    private TileExportService tileExportService;
    private TileUpdateService tileUpdateService;

    @Override
    public void onEnable() {
        this.changedChunks = new ChangedChunks();
        this.tileExportService = new TileExportService(this);
        this.tileUpdateService = new TileUpdateService(tileExportService, this, changedChunks);

        setupCommands();
        setupListeners();
        setupSchedulers();
        getLogger().info("Plugin WorldToMap enabled.");
    }

    @Override
    public void onDisable() {
        tileUpdateService.updateTiles();
        getLogger().info("WorldToMap has been disabled!");
    }

    private void setupCommands() {
        PluginCommand exportWorldTileCommand = getCommand("export-world-tile");
        PluginCommand exportRegionTileCommand = getCommand("export-region-tile");
        PluginCommand exportChunkTileCommand = getCommand("export-chunk-tile");

        if (exportWorldTileCommand == null) {
            getLogger().warning("Command /export-world-tile don't registered in plugin.yml.");
        } else {
            exportWorldTileCommand.setExecutor(new ExportWorldTileCommand(this, tileExportService));
        }

        if (exportRegionTileCommand == null) {
            getLogger().warning("Command /export-region-tile don't registered in plugin.yml.");
        } else {
            exportRegionTileCommand.setExecutor(new ExportRegionTileCommand(this, tileExportService));
        }

        if (exportChunkTileCommand == null) {
            getLogger().warning("Command /export-chunk-tile don't registered in plugin.yml.");
        } else {
            exportChunkTileCommand.setExecutor(new ExportChunkTileCommand(this, tileExportService));
        }
    }

    private void setupListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ChunksChangesListener(changedChunks), this);
    }

    private void setupSchedulers() {
        BukkitScheduler scheduler = getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(this, () -> {
            tileUpdateService.updateTiles();
        }, 6000, 6000); //5 min, todo configurable?
    }
}