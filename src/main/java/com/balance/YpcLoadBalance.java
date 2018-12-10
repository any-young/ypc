package com.balance;

import com.balance.net.RemoteNode;
import com.balance.net.YpcURI;
import com.zk.YpcZkServer;

import java.util.List;
import java.util.Objects;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
public abstract class YpcLoadBalance implements LoadBalance {
    abstract String getBalanceName();

    public  YpcURI getUri(String className){
        List<RemoteNode> remoteNodes = YpcZkServer.REMOTE_NODES.getCache(className);
        if (Objects.isNull(remoteNodes)){
            return null;
        }
        if(remoteNodes.size() == 1){
            return remoteNodes.get(0).getUri();
        }
        return selectNode(remoteNodes);
    }
}
