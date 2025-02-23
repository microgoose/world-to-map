package io.papermc.plugin;

import io.papermc.plugin.command.controller.ExportChunkTileCommand;
import io.papermc.plugin.command.controller.ExportRegionTileCommand;
import io.papermc.plugin.command.controller.ExportWorldTileCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldToMap extends JavaPlugin {
    @Override
    public void onEnable() {
        setupCommands();
        getLogger().info("Plugin WorldToMap enabled.");

        //todo auto-updating tiles when chunks changed
    }

    @Override
    public void onDisable() {
        getLogger().info("WorldToMap has been disabled!");
    }

    private void setupCommands() {
        PluginCommand exportWorldTileCommand = getCommand("export-world-tile");
        PluginCommand exportRegionTileCommand = getCommand("export-region-tile");
        PluginCommand exportChunkTileCommand = getCommand("export-chunk-tile");

        if (exportWorldTileCommand == null) {
            getLogger().warning("Command /export-world-tile don't registered in plugin.yml.");
        } else {
            exportWorldTileCommand.setExecutor(new ExportWorldTileCommand(this));
        }

        if (exportRegionTileCommand == null) {
            getLogger().warning("Command /export-region-tile don't registered in plugin.yml.");
        } else {
            exportRegionTileCommand.setExecutor(new ExportRegionTileCommand(this));
        }

        if (exportChunkTileCommand == null) {
            getLogger().warning("Command /export-chunk-tile don't registered in plugin.yml.");
        } else {
            exportChunkTileCommand.setExecutor(new ExportChunkTileCommand(this));
        }
    }
}