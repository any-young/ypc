package com.balance;
import java.util.HashMap;
import java.util.Map;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public class LoadBalanceSelector {
    private static final Map<String, YpcLoadBalance> balanceMap = new HashMap<>();
    static{
        putBalance(new RandomBalance());
    }

    private static void putBalance(YpcLoadBalance loadBalance){
        balanceMap.put(loadBalance.getBalanceName(), loadBalance);
    }

    public static YpcLoadBalance getLoadBalance(String balance){
        return balanceMap.get(balance);
    }
}
