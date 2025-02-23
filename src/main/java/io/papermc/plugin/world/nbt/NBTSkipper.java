package io.papermc.plugin.world.nbt;

import java.nio.ByteBuffer;

public class NBTSkipper {

    public static void skipTag(ByteBuffer buffer, byte tagTypeId) {
        switch (tagTypeId) {
            case 0: return;// TAG_End
            case 1: buffer.get(); break; // TAG_Byte
            case 2: buffer.getShort(); break; // TAG_Short
            case 3: buffer.getInt(); break; // TAG_Int
            case 4: buffer.getLong(); break; // TAG_Long
            case 5: buffer.getFloat(); break; // TAG_Float
            case 6: buffer.getDouble();break; // TAG_Double
            case 7: // TAG_Byte_Array
                int byteArrayLength = buffer.getInt();
                if (byteArrayLength < 0)
                    throw new IllegalArgumentException("Invalid TAG_Byte_Array length: " + byteArrayLength);
                buffer.position(buffer.position() + byteArrayLength);
                break;
            case 8: // TAG_String
                short stringLength = buffer.getShort();
                if (stringLength < 0)
                    throw new IllegalArgumentException("Invalid TAG_String length: " + stringLength);
                buffer.position(buffer.position() + stringLength);
                break;
            case 9: // TAG_List
                byte listElementType = buffer.get();
                int listLength = buffer.getInt();
                if (listLength < 0)
                    throw new IllegalArgumentException("Invalid TAG_List length: " + listLength);
                for (int i = 0; i < listLength; i++) {
                    skipTag(buffer, listElementType);
                }
                break;
            case 10: // TAG_Compound
                while (true) {
                    if (!buffer.hasRemaining())
                        throw new IllegalArgumentException("Unexpected end of TAG_Compound");
                    byte nestedTagType = buffer.get();
                    if (nestedTagType == 0) break; // TAG_End
                    io.papermc.plugin.world.nbt.NBTReader.readTagName(buffer);
                    skipTag(buffer, nestedTagType);
                }
                break;
            case 11: // TAG_Int_Array
                int intArrayLength = buffer.getInt();
                if (intArrayLength < 0)
                    throw new IllegalArgumentException("Invalid TAG_Int_Array length: " + intArrayLength);
                buffer.position(buffer.position() + 4 * intArrayLength);
                break;
            case 12: // TAG_Long_Array
                int longArrayLength = buffer.getInt();
                if (longArrayLength < 0)
                    throw new IllegalArgumentException("Invalid TAG_Long_Array length: " + longArrayLength);
                buffer.position(buffer.position() + 8 * longArrayLength);
                break;
            default:
                throw new IllegalArgumentException("Unknown NBT tag type: " + tagTypeId);
        }
    }

    public static void skipCompound(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            byte nestedTagType = buffer.get();
            if (nestedTagType == 0) break;
            NBTReader.readTagName(buffer);
            skipTag(buffer, nestedTagType);
        }
    }

    public static void skipList(ByteBuffer buffer) {
        byte listElementType = buffer.get();
        int listLength = buffer.getInt();
        if (listLength < 0) throw new IllegalArgumentException("Invalid TAG_List length: " + listLength);

        for (int i = 0; i < listLength; i++) {
            skipTag(buffer, listElementType);
        }
    }
}
