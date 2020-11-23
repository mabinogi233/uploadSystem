package com.cqujava.uploadfile.Controller;



import com.cqujava.uploadfile.Services.fileAdm.fileAdm;
import com.cqujava.uploadfile.Services.fileAdm.filePCM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
@RequestMapping("/adm")
@Transactional
public class FileAdmController {


    @Autowired
    fileAdm adm;


    /**
     * 查询用户上传的所有文件
     * @param map
     * @return
     */
    @RequestMapping("/select")
    public String getFilesMessage(ModelMap map){
        //获取用户登录信息（uid）
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        System.out.println(user.getUsername());
        int uid = Integer.parseInt(user.getUsername());
        //查询用户上传的文件
        map.addAttribute("files",adm.getFilesMessage(uid));
        for(Map<String,String> mapx:adm.getFilesMessage(uid)){
            System.out.println(mapx.get("name"));
            System.out.println(mapx.get("uploadDate"));
            System.out.println(mapx.get("lastEditDate"));
            System.out.println(mapx.get("size"));
        }
        return "admFile";
    }

    /**
     * 删除用户文件
     * @param name
     * @return
     */
    @RequestMapping("/delete")
    public String deleteFile(@RequestParam("path")String name){
        //获取用户登录信息（uid）
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        System.out.println(user.getUsername());
        int uid = Integer.parseInt(user.getUsername());
        //拼接用户空间路径
        String upath = uid + "\\" + name;
        if(adm.deleteFileAndPCM(upath)){
            return "success";
        }
        return "error";
    }

    /**
     * 修改文件
     * @param newName
     * @param oldName
     * @return
     */
    @RequestMapping("/update")
    public String updateFile(@RequestParam("newName")String newName,@RequestParam("oldName")String oldName){
        //获取用户登录信息（uid）
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        System.out.println(user.getUsername());
        int uid = Integer.parseInt(user.getUsername());
        if(adm.updateFile(uid,newName,oldName)){
            return "success";
        }
        return "error";
    }



    /**
     * 返回编辑界面
     * @param name
     * @param map
     * @return
     */
    @RequestMapping("/edit")
    public String edit(@RequestParam("path")String name,ModelMap map){
        //现有名称
        map.addAttribute("oldName",name);
        return "edit";
    }

}
