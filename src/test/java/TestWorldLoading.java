import io.papermc.plugin.world.manager.WorldManager;
import io.papermc.plugin.world.mca.model.MCAChunk;

import java.nio.file.Path;

import static io.papermc.plugin.core.config.RegionConfig.CHUNK_SIDE;

public class TestWorldLoading {
    public static void main(String[] args) {
        Path worldPath = Path.of("C:\\Users\\mikhail\\Documents\\mserv\\world");
        WorldManager manager = new WorldManager(worldPath);

        for (int x = 0; x < CHUNK_SIDE; x++) {
            for (int z = 0; z < CHUNK_SIDE; z++) {
                MCAChunk chunk = manager.loadChunk(x, z);
                System.out.printf("Chunk: :%s; :%s; Sections count: %s; %n", chunk.x, chunk.z, chunk.sections.length);
            }
        }

        MCAChunk chunk = manager.loadChunk(0,0);

        WorldManager manager2 = new WorldManager(worldPath);
        manager2.loadRegion(0,0);
        MCAChunk chunk2 = manager2.loadChunk(0,0);

        System.out.printf("%s:%s",
            chunk.getBlockType(0,62,15), //gravel
            chunk2.getBlockType(0,62,15) //gravel
        );

        manager2.loadRegion(-19, -19);
    }
}
