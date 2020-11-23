package com.cqujava.uploadfile.Controller;


import com.cqujava.uploadfile.Services.Users.user;
import com.cqujava.uploadfile.Services.Users.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@Controller
@Transactional
@RequestMapping("/user")
public class UserController {

    @Autowired
    userMapper mapper;


    /**
     * 登录验证
     * @param id
     * @param password
     * @return
     */
    @RequestMapping("/loginCheck")
    public String loginCheck(@RequestParam("uid")String id,@RequestParam("password")String password){
        try {
            int uid = Integer.parseInt(id);
            user user1 = mapper.selectByPrimaryKey(uid);
            if (user1 != null) {
                if (user1.getPassword().equals(password)) {
                    return "redirect:/adm/select?id="+id;
                }
            }
            return "loginError";
        }catch (Exception e){
            e.printStackTrace();
            return "loginError";
        }
    }

    /**
     * 注册验证
     * @param name
     * @param password
     * @return
     */
    @RequestMapping("/registerCheck")
    public String registerCheck(@RequestParam("name")String name, @RequestParam("password")String password, ModelMap map){
        //随机生成uid
        int uid;
        do {
            uid = new Random().nextInt(10000);
        }while (mapper.selectByPrimaryKey(uid)!=null);
        user user1 = new user();
        user1.setUid(uid);
        user1.setPassword(password);
        user1.setUname(name);
        mapper.insert(user1);
        map.addAttribute("uid",uid);
        return "registerSuccess";
    }

    /**
     * 返回登录界面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    /**
     * 返回注册界面
     * @return
     */
    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    /**
     * 返回登录错误页面
     * @return
     */
    @RequestMapping("/error")
    public String fail(){
        return "loginError";
    }

    //登出操作
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        //重定向到指定页面
        return "redirect:/user/login";
    }

}
