package com.proxy.server;

import com.netty.message.Result;
import com.netty.message.YpcInvocation;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public interface ServerProxy {
    Result invoke(YpcInvocation ypcInvocation);
}
