package com.config.parser;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/7
 */
public enum AttributeEnum {
    ZK_ADDRESS("zkAddress"),
    PROTOCOL("protocol"),
    PROXY("proxy"),
    LOAD_BALANCE("loadBalance"),
    PORT("port");
    private String value;
    AttributeEnum(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
