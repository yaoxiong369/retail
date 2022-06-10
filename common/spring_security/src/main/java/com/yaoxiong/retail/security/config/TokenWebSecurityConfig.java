package com.yaoxiong.retail.security.config;


import com.yaoxiong.retail.redis.utils.RedisUtil;
import com.yaoxiong.retail.security.component.DefaultPasswordEncoder;
import com.yaoxiong.retail.security.component.TokenLogoutHandler;
import com.yaoxiong.retail.base.component.TokenManager;
import com.yaoxiong.retail.security.component.UnauthEntryPoint;
import com.yaoxiong.retail.security.filter.TokenAuthFilter;
import com.yaoxiong.retail.security.filter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.session.Session;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {

    private TokenManager tokenManager;
    private RedisUtil redisUtil;
    private DefaultPasswordEncoder defaultPasswordEncoder;
    private UserDetailsService userDetailsService;

    @Autowired
    public TokenWebSecurityConfig(UserDetailsService userDetailsService, DefaultPasswordEncoder defaultPasswordEncoder,
                                  TokenManager tokenManager, RedisUtil redisUtil) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.tokenManager = tokenManager;
        this.redisUtil = redisUtil;
    }

    /**
     * configuration setting
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthEntryPoint())//No access
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and().logout().logoutUrl("/admin/acl/index/logout")//Exit path
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisUtil)).and()
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisUtil))
                .addFilter(new TokenAuthFilter(authenticationManager(), tokenManager, redisUtil)).httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    //The path without authentication can be accessed directly
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**").antMatchers("/swagger**");
    }



}
