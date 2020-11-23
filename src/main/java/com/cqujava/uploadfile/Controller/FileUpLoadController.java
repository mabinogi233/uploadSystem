package com.cqujava.uploadfile.Controller;


import com.cqujava.uploadfile.BeanConfig;
import com.cqujava.uploadfile.Services.fileAdm.filePCM;
import com.cqujava.uploadfile.Services.fileAdm.filePCMMapper;
import com.cqujava.uploadfile.Services.fileUpLoad.uploadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
@Transactional
public class FileUpLoadController {

    @Autowired
    uploadServices upload;

    //分块的进度条
    @RequestMapping(value = "/FileUploadStatus",method = RequestMethod.POST)
    @ResponseBody
    public String getStatus(HttpServletRequest request){
        long uploadPercent = 0;
        if(request.getSession().getAttribute("uploadPercent")!=null){
            uploadPercent = (long) request.getSession().getAttribute("uploadPercent");
        }
        return String.valueOf(uploadPercent);
    }


    @RequestMapping("/FileUpload")
    @ResponseBody
    public Map<String, Object> uploadfiles(@RequestParam("file") MultipartFile file,
                                           @RequestParam("fileName") String fileName,
                                           @RequestParam("seq") int seq){

        System.out.println("文件上传");
        return upload.uploadDivPackage(file,fileName,seq);

    }

    /**
     * 合并文件包，创建PCM
     * @param fileName
     * @param uid
     * @param n
     * @return
     */

    @RequestMapping("/recombination")
    @ResponseBody
    public Map<String, Object> recombination(@RequestParam("fileName")String fileName,
                                             @RequestParam("id") int uid,
                                            @RequestParam("n")int n){
        try {
            Map<String, Object> map = upload.recombination(uid, fileName, n);
            return map;
        }catch (Exception e) {
            return new HashMap<String, Object>();
        }

    }

    /**
     * 返回上传文件页面
     * @return
     */
    @RequestMapping("/index")
    public String index(ModelMap map){
        //获取用户登录信息（uid）
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        System.out.println(user.getUsername());
        int uid = Integer.parseInt(user.getUsername());
        System.out.println("上传页面");
        map.addAttribute("uid",uid);
        return "upfile";
    }
}
