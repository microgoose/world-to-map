package io.papermc.plugin.command.controller;

import io.papermc.plugin.command.service.TileExportService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ExportChunkTileCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final TileExportService tileExportService;

    public ExportChunkTileCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tileExportService = new TileExportService(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /generate-chunk <world> <chunkX> <chunkZ>");
            return false;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage("World " + worldName + " not found!");
            return false;
        }

        try {
            int chunkX = Integer.parseInt(args[1]);
            int chunkZ = Integer.parseInt(args[2]);

            sender.sendMessage("Generating chunk [" + chunkX + ", " + chunkZ + "] in world: " + worldName);
            Bukkit.getScheduler()
                    .runTaskAsynchronously(plugin, () -> tileExportService.generateChunkMap(world, chunkX, chunkZ));
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Chunk coordinates must be numbers!");
            return false;
        }
    }
}