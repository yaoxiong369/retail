package com.yaoxiong.retail.security.filter;


import com.yaoxiong.retail.redis.utils.RedisUtil;
import com.yaoxiong.retail.base.component.TokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisUtil redisUtil;
    public TokenAuthFilter(AuthenticationManager authenticationManager,TokenManager tokenManager,RedisUtil redisUtil) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisUtil = redisUtil;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Obtain the permission information of the user with successful authentication
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);

        if(authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //Get token from header
        String token = request.getHeader("token");
        if(token != null) {
            //Get user name from token
            String username = tokenManager.getUserInfoFromToken(token);

//            List<String> permissionValueList = (List<String>)redisUtil.get(username);
            Collection<GrantedAuthority> authority = new ArrayList<>();
//            for(String permissionValue : permissionValueList) {
//                SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permissionValue);
//                authority.add(auth);
//            }
            return new UsernamePasswordAuthenticationToken(username,token,authority);
        }
        return null;
    }

}
