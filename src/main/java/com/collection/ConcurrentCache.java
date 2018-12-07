package com.collection;

import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
public class ConcurrentCache<K, V> implements Cache<K, V> {
    private ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<K, V>();

    @Override
    public V getCache(K k) {
        return cache.get(k);
    }

    @Override
    public void removeCache(K k) {
        cache.remove(k);
    }

    @Override
    public void setCache(K k, V v) {
        cache.put(k, v);
    }

    @Override
    public void putIfAbsentCache(K k, V v) {
        cache.putIfAbsent(k, v);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ",  "[", "]")
                .add("cache=" + cache)
                .toString();
    }
}
