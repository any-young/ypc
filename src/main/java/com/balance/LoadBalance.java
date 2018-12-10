package com.balance;

import com.balance.net.RemoteNode;
import com.balance.net.YpcURI;

import java.util.List;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public interface LoadBalance {
    YpcURI selectNode(List<RemoteNode> remoteNodes);
}
