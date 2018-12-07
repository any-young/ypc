package com.zk.event;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/7
 */
public interface YpcEventHandler<T> {

    void nodeAdd(String path, T t);

    void nodeUpdate(String path, T t);

    void nodeDelete(String path);
}
