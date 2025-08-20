package org.theCoderFromHell.decorator;

public class CacheStatistics {
    private final int hits;
    private final int misses;
    private final int puts;
    private final int removes;

    public CacheStatistics(int hits, int misses, int puts, int removes) {
        this.hits = hits;
        this.misses = misses;
        this.puts = puts;
        this.removes = removes;
    }

    public double getHitRatio() {
        int total = hits + misses;
        return total == 0 ? 0 : (double) hits / total;
    }

    // Getters
    public int getHits() { return hits; }
    public int getMisses() { return misses; }
    public int getPuts() { return puts; }
    public int getRemoves() { return removes; }
}
