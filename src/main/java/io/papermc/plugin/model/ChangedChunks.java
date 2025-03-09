package io.papermc.plugin.model;

import org.bukkit.World;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangedChunks {
    private final Set<ChunkCoordinate> changedChunks = new HashSet<>();

    public synchronized void add(World world, int x, int z) {
        changedChunks.add(new ChunkCoordinate(world, x, z));
    }

    public synchronized Set<ChunkCoordinate> getAndClear() {
        Set<ChunkCoordinate> copy = new HashSet<>(changedChunks);
        changedChunks.clear();
        return Collections.unmodifiableSet(copy);
    }
}