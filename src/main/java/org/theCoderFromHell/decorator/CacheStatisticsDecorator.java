package org.theCoderFromHell.decorator;

import org.theCoderFromHell.core.Cache;

public class CacheStatisticsDecorator<K, V> implements Cache<K, V> {
    private final Cache<K, V> decoratedCache;
    private int hitCount = 0;
    private int missCount = 0;
    private int putCount = 0;
    private int removeCount = 0;

    public CacheStatisticsDecorator(Cache<K, V> cache) {
        this.decoratedCache = cache;
    }

    @Override
    public V get(K key) {
        V value = decoratedCache.get(key);
        if (value != null) {
            hitCount++;
        } else {
            missCount++;
        }
        return value;
    }

    @Override
    public void put(K key, V value, long ttlMillis) {
        decoratedCache.put(key, value, ttlMillis);
        putCount++;
    }

    @Override
    public void remove(K key) {
        decoratedCache.remove(key);
        removeCount++;
    }

    @Override
    public void clear() {
        decoratedCache.clear();
        resetStatistics();
    }

    @Override
    public int size() {
        return decoratedCache.size();
    }

    @Override
    public boolean containsKey(K key) {
        return decoratedCache.containsKey(key);
    }

    public CacheStatistics getStatistics() {
        return new CacheStatistics(hitCount, missCount, putCount, removeCount);
    }

    public void resetStatistics() {
        hitCount = 0;
        missCount = 0;
        putCount = 0;
        removeCount = 0;
    }
}
