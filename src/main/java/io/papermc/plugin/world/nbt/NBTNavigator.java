package io.papermc.plugin.world.nbt;

import java.nio.ByteBuffer;

public class NBTNavigator {

    public static void moveToTag(ByteBuffer buffer, int type, String tag) {
        while (buffer.hasRemaining()) {
            byte tagType = buffer.get();
            if (tagType == 0) break;

            String tagName = NBTReader.readTagName(buffer);
            if (tagType == type && tagName.equals(tag)) {
                return;
            } else {
                NBTSkipper.skipTag(buffer, tagType);
            }
        }

        throw new IllegalArgumentException("Tag not found: " + tag);
    }
}
