package io.papermc.plugin.world.manager;

import io.papermc.plugin.world.mca.loader.MCAChunkLoader;
import io.papermc.plugin.world.mca.loader.MCARegionLoader;
import io.papermc.plugin.world.mca.model.MCAChunk;
import io.papermc.plugin.world.mca.model.MCARegion;

import java.nio.file.Files;
import java.nio.file.Path;

public class WorldManager {
    private final Path regionPath;
    private final RegionCache regionCache;

    public WorldManager(Path worldPath) {
        this.regionPath = worldPath.resolve("region");
        regionCache = new io.papermc.plugin.world.manager.RegionCache(CacheConfig.MAX_REGIONS);
    }

    public WorldManager(Path worldPath, RegionCache regionCache) {
        this.regionPath = worldPath.resolve("region");
        this.regionCache = regionCache;
    }

    public MCAChunk loadChunk(int chunkX, int chunkZ) {
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;

        MCARegion region = openRegion(regionX, regionZ);
        if (region == null) return null;

        int chunkLocalX = chunkX & 31;
        int chunkLocalZ = chunkZ & 31;

        MCAChunk chunk = region.getChunk(chunkLocalX, chunkLocalZ);
        if (chunk != null) return chunk;

        Path regionPath = getRegionPath(regionX, regionZ);
        chunk = MCAChunkLoader.loadChunk(regionPath, region, chunkLocalX, chunkLocalZ);
        region.setChunk(chunkLocalX, chunkLocalZ, chunk);

        return chunk;
    }

    public MCARegion loadRegion(int regionX, int regionZ) {
        long regionKey = getRegionKey(regionX, regionZ);

        MCARegion region = regionCache.get(regionKey);
        if (region != null) return region;

        Path regionPath = getRegionPath(regionX, regionZ);
        if (!Files.exists(regionPath)) return null;

        region = MCARegionLoader.loadRegion(regionPath, regionX, regionZ);
        regionCache.put(regionKey, region);

        return region;
    }

    public MCARegion openRegion(int regionX, int regionZ) {
        long regionKey = getRegionKey(regionX, regionZ);

        MCARegion region = regionCache.get(regionKey);
        if (region != null) return region;

        Path regionPath = getRegionPath(regionX, regionZ);
        if (!Files.exists(regionPath)) return null;

        region = MCARegionLoader.openRegion(regionPath, regionX, regionZ);
        regionCache.put(regionKey, region);
        return region;
    }

    private Path getRegionPath(int regionX, int regionZ) {
        return regionPath.resolve("r." + regionX + "." + regionZ + ".mca");
    }

    private long getRegionKey(int regionX, int regionZ) {
        return (long) regionX << 32 | (regionZ & 0xFFFFFFFFL);
    }
}
