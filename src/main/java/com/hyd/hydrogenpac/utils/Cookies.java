package com.hyd.hydrogenpac.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Cookies {

    private Map<String, Cookie> cookies = new HashMap<>();

    public static Cookie newCookie(String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        return cookie;
    }

    public static Cookies from(HttpServletRequest request) {
        return new Cookies(request.getCookies());
    }

    private Cookies(Cookie[] cookies) {
        this.cookies = Arrays.stream(cookies).collect(Collectors.toMap(Cookie::getName, c -> c));
    }

    public Cookie getCookie(String name) {
        return this.cookies.get(name);
    }

    public String getCookieValue(String name) {
        return Optional.ofNullable(getCookie(name)).map(Cookie::getValue).orElse(null);
    }
}
