package com.cqujava.uploadfile;


import com.cqujava.uploadfile.Services.fileUpLoad.MyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

@Configuration
public class BeanConfig {
    //储存空间根目录
    public static String rootPath = "D:\\试验田\\javaproject";

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        return new MyResolver();
    }
}
