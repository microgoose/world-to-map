package io.papermc.plugin.world.scanner;

import io.papermc.plugin.world.manager.WorldManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class WorldScanner {

    private final Logger logger;
    private final Path worldFolder;
    private final WorldManager worldManager;
    private final WorldStream worldStream;

    public WorldScanner(Logger logger, Path worldFolder, WorldManager worldManager, WorldStream worldStream) {
        this.logger = logger;
        this.worldFolder = worldFolder;
        this.worldManager = worldManager;
        this.worldStream = worldStream;
    }

    public void handleWorld() {
        Path[] regionsPaths = getRegionFiles(worldFolder.resolve("region"));
        Arrays.sort(regionsPaths, (fPath, sPath) -> {
            int[] fCoords = extractRegionCoordinates(fPath);
            int[] sCoords = extractRegionCoordinates(sPath);

            int cmp = Integer.compare(fCoords[0], sCoords[0]);
            if (cmp == 0)
                return Integer.compare(fCoords[1], sCoords[1]);
            return cmp;
        });

        ChunkScanner chunkScanner = new ChunkScanner(worldManager, worldStream);
        RegionScanner regionScanner = new RegionScanner(chunkScanner);

        for (Path regionPath : regionsPaths) {
            long startTime = System.currentTimeMillis();
            logger.info("Region " + regionPath + " scanning start");

            int[] coords = extractRegionCoordinates(regionPath);

            worldManager.loadRegion(coords[0], coords[1]);
            regionScanner.scanRegion(coords[0], coords[1]);
            worldStream.afterRegion(coords[0], coords[1]);

            long deltaTime = System.currentTimeMillis() - startTime;
            logger.info(String.format("Region %s scanning done (%sms)", regionPath, deltaTime));
        }
    }

    private Path[] getRegionFiles(Path regionsDir) {
        if (!Files.exists(regionsDir))
            throw new IllegalArgumentException("Region directory does not exist: " + regionsDir);
        if (!Files.isDirectory(regionsDir))
            throw new IllegalArgumentException("Region directory is not a directory: " + regionsDir);

        try (Stream<Path> pathStream = Files.list(regionsDir)) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .toArray(Path[]::new);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading region directory: " + regionsDir, e);
        }
    }

    private int[] extractRegionCoordinates(Path regionPath) {
        String fileName = regionPath.getFileName().toString();
        Pattern pattern = Pattern.compile("r\\.(-?\\d+)\\.(-?\\d+)\\.mca");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.matches()) {
            return new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))};
        } else {
            throw new IllegalArgumentException("Invalid region file name: " + fileName);
        }
    }
}
