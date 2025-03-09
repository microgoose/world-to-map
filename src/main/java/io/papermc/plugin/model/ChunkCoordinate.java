package io.papermc.plugin.model;

import org.bukkit.World;

import java.util.Objects;

public class ChunkCoordinate {
    private final World world;
    private final int x;
    private final int z;

    public ChunkCoordinate(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCoordinate that = (ChunkCoordinate) o;
        return Objects.equals(world.getName(), that.getWorld().getName()) && x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world.getName(), x, z);
    }
}