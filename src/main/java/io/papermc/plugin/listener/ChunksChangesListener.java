package io.papermc.plugin.listener;

import io.papermc.plugin.model.ChangedChunks;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class ChunksChangesListener implements Listener {
    private final ChangedChunks changedChunksTracker;

    public ChunksChangesListener(ChangedChunks tracker) {
        this.changedChunksTracker = tracker;
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        trackChunk(event.getBlock().getChunk());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        trackChunk(event.getBlock().getChunk());
    }

    @EventHandler
    public void onTreeGrow(StructureGrowEvent event) {
        trackChunk(event.getLocation().getChunk());
    }

    private void trackChunk(Chunk chunk) {
        changedChunksTracker.add(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }
}