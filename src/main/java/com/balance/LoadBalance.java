package com.balance;

import com.balance.net.YpcURI;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
public class LoadBalance {

    public YpcURI getUri(){
        return new YpcURI("localhost","80","2000");
    }
}
