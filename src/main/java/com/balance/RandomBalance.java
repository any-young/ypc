package com.balance;

import com.balance.net.RemoteNode;
import com.balance.net.YpcURI;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public class RandomBalance extends YpcLoadBalance {
    @Override
    public String getBalanceName() {
        return "random";
    }

    @Override
    public YpcURI selectNode(List<RemoteNode> remoteNodes) {
        if (Objects.nonNull(remoteNodes) && remoteNodes.size()>0){
            int length = remoteNodes.size();
            int index = new Random().nextInt(length);
            return remoteNodes.get(index).getUri();
        }
        return null;
    }
}
