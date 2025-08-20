package cache;

import org.junit.jupiter.api.Test;
import org.theCoderFromHell.cache.TTLCache;
import org.theCoderFromHell.core.Cache;
import org.theCoderFromHell.decorator.CacheStatisticsDecorator;

import static org.junit.jupiter.api.Assertions.*;

public class TTLCacheTest {

    @Test
    void testPutAndGet() {
        Cache<String, String> cache = new TTLCache.Builder().build();
        cache.put("test", "value", 1000);
        assertEquals("value", cache.get("test"));
    }

    @Test
    void testExpiration() throws InterruptedException {
        Cache<String, String> cache = new TTLCache.Builder().build();
        cache.put("test", "value", 100);
        Thread.sleep(200);
        assertNull(cache.get("test"));
    }

    @Test
    void testRemove() {
        Cache<String, String> cache = new TTLCache.Builder().build();
        cache.put("test", "value", 1000);
        cache.remove("test");
        assertNull(cache.get("test"));
    }

    @Test
    void testStatistics() {
        CacheStatisticsDecorator<String, String> cache =
                new CacheStatisticsDecorator<>(new TTLCache.Builder().build());

        cache.put("key1", "value1", 1000);
        cache.get("key1");
        cache.get("nonexistent");

        assertEquals(1, cache.getStatistics().getHits());
        assertEquals(1, cache.getStatistics().getMisses());
        assertEquals(1, cache.getStatistics().getPuts());
    }
}
