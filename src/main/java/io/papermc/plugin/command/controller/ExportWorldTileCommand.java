package io.papermc.plugin.command.controller;

import io.papermc.plugin.command.service.TileExportService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ExportWorldTileCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final TileExportService tileExportService;

    public ExportWorldTileCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tileExportService = new TileExportService(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /generate-world <world>");
            return false;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage("World " + worldName + " not found!");
            return false;
        }

        sender.sendMessage("Generating tile map for world: " + worldName);
        Bukkit.getScheduler()
                .runTaskAsynchronously(plugin, () -> tileExportService.generateWorldMap(world));

        return true;
    }
}