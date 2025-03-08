package io.papermc.plugin.tiler.world.registries;

public enum BlockNamesRegistry {
    AIR("minecraft:air"),
    WATER("minecraft:water");

    private final String name;

    BlockNamesRegistry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
