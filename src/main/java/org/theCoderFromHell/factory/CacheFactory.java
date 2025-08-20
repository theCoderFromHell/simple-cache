package org.theCoderFromHell.factory;

import org.theCoderFromHell.core.Cache;
import org.theCoderFromHell.cache.TTLCache;

import java.util.concurrent.TimeUnit;

public class CacheFactory {
    public static <K, V> Cache<K, V> createDefaultCache() {
        return new TTLCache.Builder().build();
    }

    public static <K, V> Cache<K, V> createCacheWithCustomCleanup(long interval, TimeUnit unit) {
        return new TTLCache.Builder()
                .cleanupInterval(interval, unit)
                .build();
    }

    public static <K, V> Cache<K, V> createCacheWithCapacity(int capacity) {
        return new TTLCache.Builder()
                .initialCapacity(capacity)
                .build();
    }
}
