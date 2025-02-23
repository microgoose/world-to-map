package io.papermc.plugin.world.manager;

import io.papermc.plugin.world.mca.model.MCARegion;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RegionCache {
    private final int maxRegions;
    private final ConcurrentHashMap<Long, MCARegion> cache;
    private final ConcurrentLinkedQueue<Long> accessOrder;

    public RegionCache(int maxRegions) {
        this.maxRegions = maxRegions;
        this.cache = new ConcurrentHashMap<>();
        this.accessOrder = new ConcurrentLinkedQueue<>();
    }

    public MCARegion get(long key) {
        return cache.get(key);
    }

    public void put(long key, MCARegion region) {
        cache.put(key, region);
        accessOrder.add(key);

        if (cache.size() > maxRegions) {
            evictOldest();
        }
    }

    private void evictOldest() {
        Long oldestKey = accessOrder.poll();
        if (oldestKey != null) cache.remove(oldestKey);
    }
}