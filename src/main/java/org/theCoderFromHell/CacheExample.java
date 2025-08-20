package org.theCoderFromHell;

import org.theCoderFromHell.cache.TTLCache;
import org.theCoderFromHell.core.Cache;
import org.theCoderFromHell.decorator.CacheStatisticsDecorator;
import org.theCoderFromHell.factory.CacheFactory;
import java.util.concurrent.TimeUnit;

public class CacheExample {
    public static void main(String[] args) {
        // Using Builder pattern
        Cache<String, String> cache = new TTLCache.Builder()
                .cleanupInterval(2, TimeUnit.SECONDS)
                .initialCapacity(100)
                .build();

        // Using Factory pattern
        Cache<String, Integer> factoryCache = CacheFactory.createDefaultCache();

        // Using Decorator pattern for statistics
        Cache<String, String> cacheWithStats = new CacheStatisticsDecorator<>(
                new TTLCache.Builder().build()
        );

        // Example usage
        cache.put("key1", "value1", 3000); // 3 seconds TTL
        cache.put("key2", "value2", 10000); // 10 seconds TTL

        System.out.println("Key1: " + cache.get("key1")); // Returns value1
        System.out.println("Key2: " + cache.get("key2")); // Returns value2

        // Wait for expiration
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Key1 after 4s: " + cache.get("key1")); // Returns null (expired)
        System.out.println("Key2 after 4s: " + cache.get("key2")); // Returns value2

        // Cleanup
        if (cache instanceof TTLCache) {
            ((TTLCache<String, String>) cache).shutdown();
        }
    }
}