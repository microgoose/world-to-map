package io.papermc.plugin.renderer.common;

public interface RenderArea {

    int getHeight(int localX, int localZ);

    int getMapColor(int localX, int y, int localZ);

    boolean isWater(int localX, int y, int localZ);

    boolean isPlant(int localX, int y, int localZ);

    boolean isSolid(int localX, int y, int localZ);
}
