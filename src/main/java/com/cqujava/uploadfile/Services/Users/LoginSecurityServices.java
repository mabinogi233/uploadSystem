package com.cqujava.uploadfile.Services.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginSecurityServices implements UserDetailsService {

    @Autowired
    userMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        user user1 = mapper.selectByPrimaryKey(Integer.parseInt(username));
        String password = passwordEncoder.encode(user1.getPassword());
        // 封装用户信息，并返回。参数分别是：用户名，密码，用户权限
        User user = new User(username,password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return user;
    }
}