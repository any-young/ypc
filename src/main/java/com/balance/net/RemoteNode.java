package com.balance.net;

import lombok.Data;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
@Data
public class RemoteNode {

    private String nodeName;
    private YpcURI uri;

    public RemoteNode(){
        super();
    }

    public RemoteNode(String nodeName, YpcURI uri){
        this.nodeName = nodeName;
        this.uri = uri;
    }
}
