package com.netty.message;

import com.balance.net.YpcURI;

import java.io.Serializable;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getIntf() {
        return intf;
    }

    public void setIntf(Class<?> intf) {
        this.intf = intf;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isReturnType() {
        return returnType;
    }

    public void setReturnType(boolean returnType) {
        this.returnType = returnType;
    }

    public YpcURI getYpcURI() {
        return ypcURI;
    }

    public void setYpcURI(YpcURI ypcURI) {
        this.ypcURI = ypcURI;
    }
}
