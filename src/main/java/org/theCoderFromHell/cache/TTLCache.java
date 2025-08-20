package org.theCoderFromHell.cache;
import org.theCoderFromHell.core.Cache;
import org.theCoderFromHell.core.CacheEntry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TTLCache<K, V> implements Cache<K, V> {
    private final Map<K, CacheEntry<V>> cache;
    private final ScheduledExecutorService cleanupScheduler;
    private final long cleanupIntervalMillis;

    // Private constructor for Builder pattern
    private TTLCache(long cleanupIntervalMillis, int initialCapacity) {
        this.cache = new ConcurrentHashMap<>(initialCapacity);
        this.cleanupIntervalMillis = cleanupIntervalMillis;
        this.cleanupScheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleCleanup();
    }

    @Override
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null)
            return null;
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }

    @Override
    public void put(K key, V value, long ttlMillis) {
        if (ttlMillis <= 0)
            throw new IllegalArgumentException("TTL must be positive");
        CacheEntry<V> entry = new CacheEntry<>(value, ttlMillis);
        cache.put(key, entry);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean containsKey(K key) {
        CacheEntry<V> entry = cache.get(key);
        return entry != null && !entry.isExpired();
    }

    private void scheduleCleanup() {
        cleanupScheduler.scheduleAtFixedRate(
                this::cleanupExpiredEntries,
                cleanupIntervalMillis,
                cleanupIntervalMillis,
                TimeUnit.MILLISECONDS
        );
    }

    private void cleanupExpiredEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public void shutdown() {
        cleanupScheduler.shutdown();
        try {
            if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Builder pattern for flexible cache creation
    public static class Builder {
        private long cleanupInterval = 5000; // default 5 seconds
        private int initialCapacity = 16;

        public Builder cleanupInterval(long interval, TimeUnit unit) {
            this.cleanupInterval = unit.toMillis(interval);
            return this;
        }

        public Builder initialCapacity(int capacity) {
            this.initialCapacity = capacity;
            return this;
        }

        public <K, V> TTLCache<K, V> build() {
            return new TTLCache<>(cleanupInterval, initialCapacity);
        }
    }
}
