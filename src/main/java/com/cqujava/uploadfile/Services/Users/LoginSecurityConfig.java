package com.cqujava.uploadfile.Services.Users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //禁用跨站csrf攻击防御
                .formLogin()
                .loginPage("/user/login")//用户未登录时，访问任何资源都转跳到该路径，即登录页面
                .failureUrl("/user/error")
                .loginProcessingUrl("/user/loginCheck")//登录表单form中action的地址，也就是处理认证请求的路径
                .usernameParameter("uid")///登录表单form中用户名输入框input的name名，不修改的话默认是username
                .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password
                .successForwardUrl("/adm/select")//登录认证成功后默认转跳的路径
                .and()
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()//不需要通过登录验证就可以被访问的资源路径
                .antMatchers("/user/loginCheck").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/registerCheck").permitAll()
                .antMatchers("/user/error").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}