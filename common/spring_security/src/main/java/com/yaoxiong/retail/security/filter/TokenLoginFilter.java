package com.yaoxiong.retail.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoxiong.retail.base.utils.ResponseUtil;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.redis.utils.RedisUtil;
import com.yaoxiong.retail.base.component.TokenManager;
import com.yaoxiong.retail.security.entity.SecurityUser;
import com.yaoxiong.retail.security.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisUtil redisUtil;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisUtil redisUtil) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.redisUtil = redisUtil;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/acl/login","POST"));
    }

    //Submit username and password
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),
                    new ArrayList<>()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //Authenticating successfully invoked methods
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        //User information after successful authentication
        SecurityUser user = (SecurityUser)authResult.getPrincipal();

        //Generate token based on user name
        String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());

        request.getSession().setAttribute("token",token);
        request.getSession().setAttribute("permission",user.getPermissionValueList());
        System.out.println("id = " + request.getSession().getId());

        ResponseUtil.out(response, Result.ok().data("object",user.getCurrentUserInfo().getUsername()));
    }

    //Authentication failed method called
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        ResponseUtil.out(response, Result.error());
    }
}
