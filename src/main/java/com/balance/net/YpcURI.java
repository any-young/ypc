package com.balance.net;

import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
@Data
public class YpcURI implements Serializable {

    private String remoteAddress;
    private String port;
    private String timeout;
    public YpcURI(String remoteAddress, String port, String timeout){
        this.remoteAddress = remoteAddress;
        this.port = port;
        this.timeout = timeout;
    }

    public YpcURI(String remoteAddress, String port){
        this(remoteAddress, port, "3000");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", YpcURI.class.getSimpleName() + "[", "]")
                .add("remoteAddress='" + remoteAddress + "'")
                .add("port='" + port + "'")
                .add("timeout='" + timeout + "'")
                .toString();
    }
}
