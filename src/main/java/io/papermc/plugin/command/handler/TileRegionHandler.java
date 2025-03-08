package io.papermc.plugin.command.handler;

import io.papermc.plugin.exporter.TileExporter;
import io.papermc.plugin.world.scanner.common.RegionScanHandler;

public class TileRegionHandler implements RegionScanHandler {

    private final TileExporter tileExporter;

    public TileRegionHandler(TileExporter tileExporter) {
        this.tileExporter = tileExporter;
    }

    @Override
    public void onScanningDone(int regionX, int regionZ) {
        tileExporter.exportAll();
    }
}
