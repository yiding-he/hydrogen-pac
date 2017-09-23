package com.hyd.hydrogenpac.beans;

/**
 * (description)
 * created at 2017/8/12
 *
 * @author yidin
 */
public class Proxy {

    private String proxyName;

    private String proxyType;

    private String proxyAddress;

    public Proxy() {
    }

    public Proxy(String proxyName, String proxyType, String proxyAddress) {
        this.proxyName = proxyName;
        this.proxyType = proxyType;
        this.proxyAddress = proxyAddress;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }
}
