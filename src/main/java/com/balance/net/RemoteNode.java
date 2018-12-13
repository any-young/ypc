package com.balance.net;


/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public YpcURI getUri() {
        return uri;
    }

    public void setUri(YpcURI uri) {
        this.uri = uri;
    }
}
