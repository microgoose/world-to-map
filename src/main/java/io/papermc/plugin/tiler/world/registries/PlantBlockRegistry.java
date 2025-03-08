package io.papermc.plugin.tiler.world.registries;

import org.bukkit.Material;

import java.util.EnumSet;

public class PlantBlockRegistry {
    public static final EnumSet<Material> ALL_PLANT_BLOCKS = EnumSet.of(
            Material.FERN,
            Material.LARGE_FERN,
            Material.DEAD_BUSH,
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.PINK_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE,
            Material.SUNFLOWER,
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING,
            Material.BIRCH_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.ACACIA_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.MANGROVE_PROPAGULE,
            Material.SWEET_BERRY_BUSH,
            Material.CAVE_VINES,
            Material.VINE,
            Material.TWISTING_VINES,
            Material.WEEPING_VINES,
            Material.SUGAR_CANE,
            Material.CACTUS,
            Material.BAMBOO,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.MUSHROOM_STEM,
            Material.MOSS_BLOCK,
            Material.MOSS_CARPET,
            Material.AZALEA,
            Material.FLOWERING_AZALEA,
            Material.WARPED_FUNGUS,
            Material.CRIMSON_FUNGUS,
            Material.WARPED_ROOTS,
            Material.CRIMSON_ROOTS,
            Material.NETHER_SPROUTS,
            Material.BRAIN_CORAL,
            Material.BUBBLE_CORAL,
            Material.FIRE_CORAL,
            Material.HORN_CORAL,
            Material.TUBE_CORAL
    );

    public static final EnumSet<Material> WATER_PLANT_BLOCKS = EnumSet.of(
            Material.SEAGRASS,
            Material.TALL_SEAGRASS,
            Material.KELP,
            Material.KELP_PLANT,
            Material.LILY_PAD,
            Material.CHORUS_PLANT,
            Material.CHORUS_FLOWER,
            Material.BRAIN_CORAL,
            Material.BUBBLE_CORAL,
            Material.FIRE_CORAL,
            Material.HORN_CORAL,
            Material.TUBE_CORAL,
            Material.DEAD_BRAIN_CORAL,
            Material.DEAD_BUBBLE_CORAL,
            Material.DEAD_FIRE_CORAL,
            Material.DEAD_HORN_CORAL,
            Material.DEAD_TUBE_CORAL
    );

    public static final EnumSet<Material> GRASS_PLANT_BLOCKS = EnumSet.of(
            Material.SHORT_GRASS,
            Material.TALL_GRASS
    );

    public static boolean isPlant(Material type) {
        return ALL_PLANT_BLOCKS.contains(type) ||
                WATER_PLANT_BLOCKS.contains(type) ||
                GRASS_PLANT_BLOCKS.contains(type);
    }
}
