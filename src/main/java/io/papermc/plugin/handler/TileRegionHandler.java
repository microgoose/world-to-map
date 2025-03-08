package io.papermc.plugin.handler;

import io.papermc.plugin.tiler.map.exporter.TileExporter;
import io.papermc.plugin.tiler.world.scanner.common.RegionScanHandler;

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
