package com.hyd.hydrogenpac.oauth;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
public class OAuthException extends RuntimeException {

    public OAuthException() {
    }

    public OAuthException(String message) {
        super(message);
    }

    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthException(Throwable cause) {
        super(cause);
    }
}
