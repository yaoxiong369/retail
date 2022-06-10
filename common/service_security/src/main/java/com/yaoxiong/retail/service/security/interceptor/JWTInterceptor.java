package com.yaoxiong.retail.service.security.interceptor;

import com.yaoxiong.retail.base.component.TokenManager;
import com.yaoxiong.retail.base.exceptionhandler.TokenParsingFailedException;
import com.yaoxiong.retail.service.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Autowired
    TokenManager tokenManager;

    public final static ThreadLocal<User> userInfo = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader("token");
        try{
            String username = tokenManager.getUserInfoFromToken(token);
            User user = new User(null,username);
            userInfo.set(user);
//            request.setAttribute("username",username);
            return true;
        }catch (Exception e){
            throw new TokenParsingFailedException(20002,"Token Parsing Failed Exception: "+ e.getMessage());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        userInfo.remove();
    }

}
