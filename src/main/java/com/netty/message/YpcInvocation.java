package com.netty.message;

import com.balance.net.YpcURI;
import lombok.Data;

import java.io.Serializable;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
@Data
public class YpcInvocation implements Serializable {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 12223L;
    private String serialNo;//唯一码，用作接口判断是否返回作用
    private String methodName;//执行的方法名
    private String className;//加载的类名称
    private Class<?> intf;//实现接口
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String timeout = "3000";
    private String protocol = "HESSIAN";
    private boolean returnType;
    private YpcURI ypcURI;//执行服务的url
}
