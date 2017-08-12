package com.hyd.hydrogenpac.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class Cookies {

    private Map<String, Cookie> cookies = new HashMap<>();

    public static Cookie newCookie(String name, String value, String domain, int expiry) {
        Cookie cookie = new Cookie(name, value);
        if (Str.notEmpty(domain)) cookie.setDomain(domain);
        cookie.setMaxAge(expiry);
        return cookie;
    }

    public static Cookies from(HttpServletRequest request) {
        return new Cookies(request.getCookies());
    }

    private Cookies(Cookie[] cookies) {
        if (cookies == null) {
            this.cookies = Collections.emptyMap();
        } else {
            this.cookies = Arrays.stream(cookies).collect(Collectors.toMap(Cookie::getName, c -> c));
        }
    }

    public Cookie getCookie(String name) {
        return this.cookies.get(name);
    }

    public String getCookieValue(String name) {
        return Optional.ofNullable(getCookie(name)).map(Cookie::getValue).orElse(null);
    }
}
