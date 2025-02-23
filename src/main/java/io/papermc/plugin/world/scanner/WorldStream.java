package io.papermc.plugin.world.scanner;

import io.papermc.plugin.world.mca.model.MCAChunk;

public interface WorldStream {

    void handleChunk(MCAChunk chunk);

    void afterRegion(int regionX, int regionZ);

}
