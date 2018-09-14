package com.hyd.hydrogenpac.oauth;

import com.hyd.hydrogenpac.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
public abstract class OAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(OAuthService.class);

    private static Map<OAuthChannel, OAuthService> serviceMappings = new HashMap<>();

    public static OAuthService getOAuthService(OAuthChannel type) {
        return serviceMappings.get(type);
    }

    @PostConstruct
    private void init() {
        register();
    }

    protected OAuthChannel getOauthChannel() {
        Class<? extends OAuthService> type = this.getClass();

        if (!type.isAnnotationPresent(OAuth.class)) {
            return null;
        } else {
            return type.getAnnotation(OAuth.class).type();
        }
    }

    private void register() {
        OAuthChannel type = getOauthChannel();

        if (type == null) {
            LOG.error(getClass() + " is not annotated with @OAuth!");
        } else {
            serviceMappings.put(type, this);
            LOG.info("OAuth service " + getClass().getCanonicalName() + " is registered as type '" + type + "'");
        }
    }

    //////////////////////////////////////////////////////////////

    public abstract OAuthEntry getOAuthEntry(String requestUrl);

    public abstract User getUser(String authCode, String requestUrl);

    public abstract boolean isEnabled();
}
