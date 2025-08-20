package org.theCoderFromHell.core;
import java.time.Instant;

public class CacheEntry<V> {
    private final V value;
    private final Instant expiryTime;
    private final long ttlMillis;

    public CacheEntry(V value, long ttlMillis) {
        this.value = value;
        this.ttlMillis = ttlMillis;
        this.expiryTime = Instant.now().plusMillis(ttlMillis);
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryTime);
    }

    public long getTtlMillis() {
        return ttlMillis;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }
}
