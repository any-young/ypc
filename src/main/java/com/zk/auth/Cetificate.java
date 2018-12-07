package com.zk.auth;

import lombok.Data;

/**
 * zookeeper的登录用户名
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
@Data
public class Cetificate {

    private String mode = "digist";
    private String user = "admin";
    private String password ="admin";

    @Override
    public String toString(){
        return this.user+":"+this.password;
    }
}
