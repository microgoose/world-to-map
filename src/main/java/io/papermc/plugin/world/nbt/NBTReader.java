package io.papermc.plugin.world.nbt;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class NBTReader {

    public static String readString(ByteBuffer buffer) {
        short length = buffer.getShort();
        if (length < 0) throw new IllegalArgumentException("Invalid TAG_String length: " + length);
        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    public static long[] readLongArray(ByteBuffer buffer) {
        int length = buffer.getInt();
        if (length < 0) throw new IllegalArgumentException("Invalid TAG_Long_Array length: " + length);
        long[] longArray = new long[length];
        for (int i = 0; i < length; i++) {
            longArray[i] = buffer.getLong();
        }
        return longArray;
    }

    public static int[] readIntArray(ByteBuffer buffer) {
        int length = buffer.getInt();
        if (length < 0) throw new IllegalArgumentException("Invalid TAG_Int_Array length: " + length);
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = buffer.getInt();
        }
        return intArray;
    }

    public static String readTagName(ByteBuffer buffer) {
        return readString(buffer);
    }
}
