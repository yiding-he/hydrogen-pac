package com.hyd.hydrogenpac.oauth;

import com.alibaba.fastjson.JSONObject;
import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.config.AuthConfig;
import com.hyd.hydrogenpac.utils.HttpRequest;
import com.hyd.hydrogenpac.utils.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
@Component
@OAuth(type = OAuthChannel.Baidu)
public class BaiduOAuthService extends OAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(BaiduOAuthService.class);

    public static final String LOGIN_URL_PATTERN = "http://openapi.baidu.com/oauth/2.0/authorize?" +
            "response_type=code&" +
            "client_id=${clientId}&" +
            "redirect_uri=${callbackUrl}&" +
            "scope=basic& display=popup";

    public static final String CALLBACK_URL_PATTERN = "${prefix}/auth/callback/baidu";

    public static final String AVATAR_URL_PATTERN = "http://tb.himg.baidu.com/sys/portraitn/item/${portrait}";

    @Autowired
    private AuthConfig authConfig;

    @Override
    public boolean isEnabled() {
        return Str.notEmpty(
                authConfig.getBaiduApiKey(),
                authConfig.getBaiduApiSecret()
        );
    }

    @Override
    public String readCode(HttpServletRequest request) {
        return request.getParameter("code");
    }

    @Override
    public OAuthEntry getOAuthEntry(String requestUrl) {

        String callbackUrl = getCallbackUrl(requestUrl);
        String loginUrl = getLoginUrl(callbackUrl);

        return new OAuthEntry(getOauthChannel(), loginUrl, callbackUrl);
    }

    private String getLoginUrl(String callbackUrl) {
        return LOGIN_URL_PATTERN
                .replace("${clientId}", authConfig.getBaiduApiKey())
                .replace("${callbackUrl}", Str.urlEnc(callbackUrl));
    }

    private String getCallbackUrl(String requestUrl) {
        int afterSchema = requestUrl.startsWith("https://") ? 8 : 7;
        String prefix = requestUrl.substring(0, requestUrl.indexOf("/", afterSchema));
        return CALLBACK_URL_PATTERN.replace("${prefix}", prefix);
    }

    @Override
    public User getUser(String authCode, String requestUrl) {
        String accessToken = getBaiduAccessToken(authCode, requestUrl);
        JSONObject baiduUserInfo = getBaiduUserInfo(accessToken);

        User user = new User();
        user.setOauthChannel(getOauthChannel());
        user.setUserId(baiduUserInfo.getString("uid"));
        user.setUsername(baiduUserInfo.getString("uname"));
        user.setAvatar(AVATAR_URL_PATTERN.replace("${portrait}", baiduUserInfo.getString("portrait")));

        return user;
    }

    private JSONObject getBaiduUserInfo(String accessToken) {

        HttpRequest getUserInfoRequest = new HttpRequest(
                "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser")
                .setParameter("access_token", accessToken);

        try {
            return JSONObject.parseObject(getUserInfoRequest.request());
        } catch (IOException e) {
            throw new OAuthException(getUserInfoRequest.getResponseString(), e);
        }
    }

    private String getBaiduAccessToken(String code, String requestUrl) {

        HttpRequest authRequest = new HttpRequest("https://openapi.baidu.com/oauth/2.0/token")
                .setParameter("grant_type", "authorization_code")
                .setParameter("code", code)
                .setParameter("client_id", authConfig.getBaiduApiKey())
                .setParameter("client_secret", authConfig.getBaiduApiSecret())
                .setParameter("redirect_uri", getCallbackUrl(requestUrl));

        try {
            JSONObject authObject = JSONObject.parseObject(authRequest.request());
            return authObject.getString("access_token");
        } catch (IOException e) {
            throw new OAuthException(authRequest.getResponseString(), e);
        }
    }
}
