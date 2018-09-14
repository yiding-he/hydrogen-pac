package com.hyd.hydrogenpac.oauth;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
public class OAuthEntry {

    private OAuthChannel type;

    private String loginUrl;

    private String callbackUrl;

    public OAuthEntry() {
    }

    public OAuthEntry(OAuthChannel type, String loginUrl, String callbackUrl) {
        this.type = type;
        this.loginUrl = loginUrl;
        this.callbackUrl = callbackUrl;
    }

    public OAuthChannel getType() {
        return type;
    }

    public void setType(OAuthChannel type) {
        this.type = type;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
