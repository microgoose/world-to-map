package io.papermc.plugin.tiler.world.scanner;

import io.papermc.plugin.tiler.world.scanner.common.RegionScanHandler;
import io.papermc.plugin.tiler.world.config.RegionConfig;

public class RegionScanner {
    private final ChunkScanner chunkScanner;
    private RegionScanHandler regionScanListener;

    public RegionScanner(ChunkScanner chunkScanner) {
        this.chunkScanner = chunkScanner;
    }

    public RegionScanner(ChunkScanner chunkScanner, RegionScanHandler regionScanListener) {
        this.chunkScanner = chunkScanner;
        this.regionScanListener = regionScanListener;
    }

    public void scanRegion(int regionX, int regionZ) {
        int startChunkX = regionX * RegionConfig.CHUNK_SIDE;
        int startChunkZ = regionZ * RegionConfig.CHUNK_SIDE;

        for (int dx = 0; dx < RegionConfig.CHUNK_SIDE; dx++) {
            for (int dz = 0; dz < RegionConfig.CHUNK_SIDE; dz++) {
                int chunkX = startChunkX + dx;
                int chunkZ = startChunkZ + dz;
                chunkScanner.scanChunk(chunkX, chunkZ);
            }
        }

        if (regionScanListener != null) {
            regionScanListener.onScanningDone(regionX, regionZ);
        }
    }
}
