package io.papermc.plugin.world.mca.loader;

import io.papermc.plugin.world.mca.model.MCASection;
import io.papermc.plugin.world.nbt.NBTNavigator;
import io.papermc.plugin.world.nbt.NBTReader;
import io.papermc.plugin.world.nbt.NBTSkipper;

import java.nio.ByteBuffer;

public class MCASectionLoader {
    public static MCASection readSection(ByteBuffer sectionData) {
        byte sectionY = Byte.MIN_VALUE;
        String[] sectionPalette = null;
        long[] sectionBlocks = null;

        while (sectionData.hasRemaining()) {
            byte tagType = sectionData.get();
            if (tagType == 0) break;
            String tagName = NBTReader.readTagName(sectionData);

            if (tagName.equals("Y")) {
                sectionY = sectionData.get();
                continue;
            }

            if (tagName.equals("block_states")) {
                while (sectionData.hasRemaining()) {
                    byte subTagType = sectionData.get();
                    if (subTagType == 0) break;
                    String subTagName = NBTReader.readTagName(sectionData);

                    if (subTagName.equals("palette")) {
                        sectionPalette = readPalette(sectionData);
                        continue;
                    }

                    if (subTagName.equals("data")) {
                        sectionBlocks = NBTReader.readLongArray(sectionData);
                        continue;
                    }

                    NBTSkipper.skipTag(sectionData, subTagType);
                }

                continue;
            }

            NBTSkipper.skipTag(sectionData, tagType);
        }

        if (sectionPalette != null && sectionPalette.length > 1 && sectionBlocks == null)
            throw new IllegalStateException("Section Blocks not initialized");

        return new MCASection(sectionY, sectionPalette, sectionBlocks);
    }

    private static String[] readPalette(ByteBuffer palettePos) {
        palettePos.get();
        int paletteCount = palettePos.getInt();
        String[] palette = new String[paletteCount];

        for (int i = 0; i < paletteCount; i++) {
            NBTNavigator.moveToTag(palettePos, 8, "Name");
            palette[i] = NBTReader.readString(palettePos);
            NBTSkipper.skipCompound(palettePos);
        }

        return palette;
    }
}
