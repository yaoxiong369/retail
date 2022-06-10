package com.yaoxiong.retail.gateway.util;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.util.Base64;

public class HttpUtil {

    public static String getSessionId(ServerHttpRequest request) {
        try {
            return new String(Base64.getDecoder().decode(request.getCookies().toSingleValueMap().get("SESSION").getValue()), "UTF-8");
        } catch (Exception e) {
            return null;
        }

    }
}
