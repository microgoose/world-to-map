package io.papermc.plugin.world.mca.loader;

import io.papermc.plugin.core.config.ChunkConfig;
import io.papermc.plugin.core.config.SectionConfig;
import io.papermc.plugin.world.mca.model.MCAChunk;
import io.papermc.plugin.world.mca.model.MCARegion;
import io.papermc.plugin.world.mca.model.MCASection;
import io.papermc.plugin.world.nbt.NBTReader;
import io.papermc.plugin.world.nbt.NBTSkipper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import static io.papermc.plugin.core.config.RegionConfig.CHUNK_SIDE;

public class MCAChunkLoader {

    public static MCAChunk loadChunk(ByteBuffer chunkBuffer) {
        byte rootTagType = chunkBuffer.get();
        if (rootTagType != 0x0A)
            throw new IllegalStateException("Invalid NBT chunk format: root is not TAG_Compound");

        NBTReader.readString(chunkBuffer);

        Integer chunkXPos = null;
        Integer chunkZPos = null;
        String status = null;
        MCASection[] chunkMCASections = null;

        while (chunkBuffer.hasRemaining()) {
            byte tagType = chunkBuffer.get();
            if (tagType == 0) break;
            String tagName = NBTReader.readTagName(chunkBuffer);

            switch (tagName) {
                case "xPos":
                    chunkXPos = chunkBuffer.getInt();
                    continue;
                case "zPos":
                    chunkZPos = chunkBuffer.getInt();
                    continue;
                case "Status":
                    status = NBTReader.readString(chunkBuffer);
                    continue;
                case "sections":
                    chunkMCASections = readSections(chunkBuffer);
                    continue;
            }

            NBTSkipper.skipTag(chunkBuffer, tagType);
        }

        if (chunkXPos == null)
            throw new IllegalStateException("Chunk X position not initialized");
        if (chunkZPos == null)
            throw new IllegalStateException("Chunk Z position not initialized");
        if (chunkMCASections == null)
            throw new IllegalStateException("Chunk sections not initialized");

        return new MCAChunk(chunkXPos, chunkZPos, status, chunkMCASections);
    }

    public static MCAChunk loadChunk(ByteBuffer chunkBuffer, int length, int compressionType) {
        if (compressionType != 2) //Zlib
            throw new RuntimeException("Unknown compression type: " + compressionType);

        byte[] compressedChunkData = new byte[length - 1];
        chunkBuffer.get(compressedChunkData);
        byte[] uncompressedChunkData = decompress(compressedChunkData);

        return loadChunk(ByteBuffer.wrap(uncompressedChunkData));
    }

    public static MCAChunk loadChunk(ByteBuffer regionBuffer, int[] offsets, int localX, int localZ) {
        int offset = getPositionOffset(offsets, localX, localZ);
        if (offset == 0) return null;

        regionBuffer.position(offset);

        int length = regionBuffer.getInt();
        int compressionType = regionBuffer.get();

        return loadChunk(regionBuffer, length, compressionType);
    }

    public static MCAChunk loadChunk(Path regionPath, MCARegion region, int localX, int localZ) {
        int offset = getPositionOffset(region.offsets, localX, localZ);
        if (offset == 0) return null;

        try (FileChannel channel = FileChannel.open(regionPath, StandardOpenOption.READ)) {
            channel.position(offset);

            ByteBuffer headerBuffer = ByteBuffer.allocate(5);
            channel.read(headerBuffer);
            headerBuffer.flip();

            int length = headerBuffer.getInt();
            int compressionType = Byte.toUnsignedInt(headerBuffer.get());

            ByteBuffer regionBuffer = ByteBuffer.allocate(length - 1);
            channel.read(regionBuffer);
            regionBuffer.flip();

            MCAChunk chunk = MCAChunkLoader.loadChunk(regionBuffer, length, compressionType);
            region.setChunk(localX, localZ, chunk);

            return chunk;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static MCAChunk loadChunk(Path regionPath, int chunkX, int chunkZ) {
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;
        int localX = chunkX & 31;
        int localZ = chunkZ & 31;
        MCARegion region = MCARegionLoader.openRegion(regionPath, regionX, regionZ);
        return loadChunk(regionPath, region, localX, localZ);
    }

    public static int getPositionOffset(int[] offsets, int localX, int localZ) {
        int index = (localX % CHUNK_SIDE) + (localZ % CHUNK_SIDE) * CHUNK_SIDE;
        return offsets[index];
    }

    private static MCASection[] readSections(ByteBuffer sectionsData) {
        byte sectionsTagType = sectionsData.get();
        int sectionsCount = sectionsData.getInt();

        if (sectionsTagType != 10)
            throw new IllegalStateException("Invalid section tag type: " + sectionsTagType);

        MCASection[] sectionArray = new MCASection[ChunkConfig.SECTIONS_COUNT]; //todo what if section count > SECTIONS_COUNT
        for (int i = 0; i < sectionsCount; i++) {
            MCASection newMCASection = MCASectionLoader.readSection(sectionsData);

            if (newMCASection.palette == null)
                continue;
            if (newMCASection.y < SectionConfig.MIN_SECTION_Y || newMCASection.y > SectionConfig.MAX_SECTION_Y)
                continue;

            sectionArray[newMCASection.y - SectionConfig.MIN_SECTION_Y] = newMCASection;
        }

        return sectionArray;
    }

    private static byte[] decompress(byte[] compressedData) {
        Inflater inflater = new Inflater();

        try (InflaterInputStream inflaterStream = new InflaterInputStream(new ByteArrayInputStream(compressedData), inflater);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inflaterStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress chunk", e);
        } finally {
            inflater.end();
        }
    }
}
