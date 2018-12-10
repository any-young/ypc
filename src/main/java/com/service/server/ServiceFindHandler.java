package com.service.server;

import com.exception.NotFoundServiceException;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
public interface ServiceFindHandler {

    public Object findObject(String name) throws NotFoundServiceException;
}
