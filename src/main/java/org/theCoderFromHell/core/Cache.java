package org.theCoderFromHell.core;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value, long ttlMillis);
    void remove(K key);
    void clear();
    int size();
    boolean containsKey(K key);
}
