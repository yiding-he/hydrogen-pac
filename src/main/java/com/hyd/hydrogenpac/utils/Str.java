package com.hyd.hydrogenpac.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
public class Str {

    public static String urlEnc(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
}
