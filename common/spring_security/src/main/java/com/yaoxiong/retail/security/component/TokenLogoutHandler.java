package com.yaoxiong.retail.security.component;

import com.yaoxiong.retail.base.component.TokenManager;
import com.yaoxiong.retail.base.utils.ResponseUtil;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.redis.utils.RedisUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisUtil redisUtil;

    public TokenLogoutHandler(TokenManager tokenManager, RedisUtil redisUtil) {
        this.tokenManager = tokenManager;
        this.redisUtil = redisUtil;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if(token != null) {
            tokenManager.removeToken(token);
        }
        ResponseUtil.out(response, Result.ok());
    }
}
