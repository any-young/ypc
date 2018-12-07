package com.collection;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
public interface Cache<K, V> {
    V getCache(K k);
    void removeCache(K k);
    void setCache(K k, V v);
    void putIfAbsentCache(K k, V v);
}
