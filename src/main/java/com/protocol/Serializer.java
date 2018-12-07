package com.protocol;

import java.io.IOException;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/6
 */
public interface Serializer {

    <T> T transToObject(Class<T> clazz, byte[] bytes) throws IOException;

    byte[] transToByte(Object object) throws IOException;

    String getName();
}
