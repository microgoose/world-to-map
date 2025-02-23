package io.papermc.plugin.world.mca.loader;

import io.papermc.plugin.world.mca.model.MCAChunk;
import io.papermc.plugin.world.mca.model.MCARegion;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static io.papermc.plugin.core.config.RegionConfig.CHUNK_SIDE;

public class MCARegionLoader {

    public static MCARegion loadRegion(Path regionPath, int regionX, int regionZ) {
        int futureIndex = 0;
        int nThreads = Math.min(32, Runtime.getRuntime().availableProcessors() * 2);

        try (ExecutorService executor = Executors.newFixedThreadPool(nThreads)) {
            byte[] file = Files.readAllBytes(regionPath);
            ByteBuffer regionBuffer = ByteBuffer.wrap(file);
            regionBuffer.order(ByteOrder.BIG_ENDIAN);

            MCARegion region = new MCARegion(regionX, regionZ, parseHeaders(regionBuffer));
            Future<?>[] futures = new Future[CHUNK_SIDE * CHUNK_SIDE];

            for (int x = 0; x < CHUNK_SIDE; x++) {
                for (int z = 0; z < CHUNK_SIDE; z++) {
                    final int localX = x;
                    final int localZ = z;

                    futures[futureIndex++] = executor.submit(() -> {
                        int offset = MCAChunkLoader.getPositionOffset(region.offsets, localX, localZ);

                        if (offset != 0) {
                            ByteBuffer chunkBuffer = regionBuffer.duplicate();
                            chunkBuffer.position(offset);

                            MCAChunk chunk = MCAChunkLoader.loadChunk(chunkBuffer, region.offsets, localX, localZ);
                            region.setChunk(localX, localZ, chunk);
                        }
                    });
                }
            }

            for (Future<?> future : futures) {
                future.get();
            }

            return region;
        } catch (Exception e) {
            throw new RuntimeException("Error during region loading", e);
        }
    }

    public static MCARegion openRegion(Path regionPath, int regionX, int regionZ) {
        try (FileChannel channel = FileChannel.open(regionPath, StandardOpenOption.READ)) {
            ByteBuffer regionBuffer = ByteBuffer.allocate(4096);
            channel.read(regionBuffer);
            regionBuffer.flip();

            return new MCARegion(regionX, regionZ, parseHeaders(regionBuffer));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static int[] parseHeaders(ByteBuffer regionBuffer) {
        if (regionBuffer.remaining() < 4096)
            throw new IllegalArgumentException("Region file header is too small");

        int[] offsets = new int[1024];
        for (int i = 0; i < 1024; i++) {
            offsets[i] = (regionBuffer.getInt() >> 8) * 4096;
        }

        return offsets;
    }
}
