package com.balance;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public enum  LoadBalanceEnum {
    RANDOM(new RandomBalance());

    YpcLoadBalance loadBalance;
    String name;
    LoadBalanceEnum(YpcLoadBalance loadBalance){
        this.loadBalance = loadBalance;
        this.name = loadBalance.getBalanceName();
    }

    public YpcLoadBalance getLoadBalance() {
        return loadBalance;
    }

    public String getName() {
        return name;
    }
}
