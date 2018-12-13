package com.zk.auth;


/**
 * zookeeper的登录用户名
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
public class Cetificate {

    private String mode = "digist";
    private String user = "admin";
    private String password ="admin";

    @Override
    public String toString(){
        return this.user+":"+this.password;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
