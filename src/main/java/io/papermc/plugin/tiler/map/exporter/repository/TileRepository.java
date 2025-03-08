package io.papermc.plugin.tiler.map.exporter.repository;

import io.papermc.plugin.tiler.map.exporter.config.TileConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TileRepository {

    private final Path outputDirectory;
    private final ConcurrentHashMap<Path, CompletableFuture<BufferedImage>> tiles;

    public TileRepository(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.tiles = new ConcurrentHashMap<>();
    }

    public BufferedImage getTile(int zoom, int tX, int tY) {
        Path tilePath = getTilePath(zoom, tX, tY);
        return tiles.computeIfAbsent(tilePath, path ->
            CompletableFuture.supplyAsync(() -> loadTile(path))
        ).join();
    }

    public void updateTile(int zoom, int tX, int tY, BufferedImage image) {
        tiles.put(getTilePath(zoom, tX, tY), CompletableFuture.completedFuture(image));
    }

    private Path getTilePath(int zoom, int tX, int tY) {
        return outputDirectory.resolve("z" + zoom + "/t." + tX + "." + tY + ".png");
    }

    public void release() {
        CompletableFuture<?>[] futures = tiles.keySet().stream()
                .map(tilePath -> CompletableFuture.runAsync(() -> exportTile(tilePath)))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
    }

    private BufferedImage loadTile(Path tilePath) {
        try {
            if (Files.exists(tilePath)) {
                return ImageIO.read(tilePath.toFile());
            } else {
                return new BufferedImage(TileConfig.TILE_SIDE, TileConfig.TILE_SIDE, BufferedImage.TYPE_INT_ARGB);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load tile image: " + tilePath, e);
        }
    }

    private void exportTile(Path tilePath) {
        try {
            BufferedImage image = tiles.remove(tilePath).join();
            Files.createDirectories(tilePath.getParent());
            ImageIO.write(image, TileConfig.IMAGE_FORMAT, tilePath.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save tile image: " + tilePath, e);
        }
    }
}