package io.papermc.plugin.tiler.map.exporter.model;

import io.papermc.plugin.tiler.world.registries.BlockNamesRegistry;
import io.papermc.plugin.tiler.world.registries.PlantBlockRegistry;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockState {
    private final Map<String, Material> materials = new ConcurrentHashMap<>();
    private final Map<String, Integer> colors = new ConcurrentHashMap<>();

    public int getMapColor(String blockType) {
        Material material = getMaterial(blockType);
        return colors.computeIfAbsent(blockType, k -> {
            BlockData blockData = material.createBlockData();
            return blockData.getMapColor().asARGB();
        });
    }

    public boolean isSolid(String blockType) {
        return getMaterial(blockType).isSolid();
    }

    public boolean isTransparent(String blockType) {
        return getMaterial(blockType).isAir();
    }

    public boolean isWater(String blockType) {
        return BlockNamesRegistry.WATER.getName().equals(blockType);
    }

    public boolean isPlant(String blockType) {
        return PlantBlockRegistry.isPlant(getMaterial(blockType));
    }

    private Material getMaterial(String blockType) {
        return materials.computeIfAbsent(blockType, k -> {
            String type = blockType.replaceAll("minecraft:", "").toUpperCase();
            Material material = Material.getMaterial(type);

            if (material == null)
                throw new IllegalArgumentException(String.format(
                    "Material %s (%s) is not a valid material", blockType, type
                ));

            return material;
        });
    }
}
