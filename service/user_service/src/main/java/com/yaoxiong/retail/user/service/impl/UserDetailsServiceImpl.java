package com.yaoxiong.retail.user.service.impl;


import com.yaoxiong.retail.security.entity.SecurityUser;
import com.yaoxiong.retail.user.entity.User;
import com.yaoxiong.retail.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Query data by user name
        User user = userService.selectByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("user does not exist");
        }
        com.yaoxiong.retail.security.entity.User curUser = new com.yaoxiong.retail.security.entity.User();
        BeanUtils.copyProperties(user, curUser);


        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);
        return securityUser;
    }
}
