package com.cqujava.uploadfile.Services.fileUpLoad;

import com.cqujava.uploadfile.BeanConfig;
import com.cqujava.uploadfile.Services.fileAdm.filePCM;
import com.cqujava.uploadfile.Services.fileAdm.filePCMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class uploadServices {

    @Autowired
    filePCMMapper mapper;

    //存储空间根目录
    String rootPath = BeanConfig.rootPath;

    /**
     * 重组文件包
     * @param uid
     * @param fileName
     * @param n
     * @return
     */
    public Map<String,Object> recombination(int uid,String fileName,int n) throws RuntimeException{
        //n为拆分的个数
        //整合并从临时文件夹中提取到用户空间文件夹
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            String path = rootPath + "\\" + Integer.toString(uid);
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            //要输出的文件
            File outputFile = new File(path + "\\" + fileName);
            FileOutputStream out = new FileOutputStream(outputFile);
            //找出输入的文件（后缀为1-n的n个文件）
            String inputFilePath = rootPath + "\\temporary\\" + fileName;
            //按顺序输出到文件
            byte[] bytes = new byte[1024*1024];
            for (int i=1;i<=n;i++) {
                File inputFile = new File(inputFilePath + "--" + Integer.toString(i));
                FileInputStream in = new FileInputStream(inputFile);
                System.out.println(inputFilePath + "--" + Integer.toString(i));
                int len = -1;
                while((len = in.read(bytes))!=-1){
                    out.write(bytes, 0, len);
                }
                in.close();
                //删除合并前文件
                inputFile.delete();
            }
            out.close();

            //创建PCM
            Date date = new Date();//获取当前的日期
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String str = df.format(date);//获取String类型的时间

            filePCM pcm = new filePCM();
            pcm.setPath(path + "\\" + fileName);
            pcm.setLasteditdate(str);
            pcm.setUploaddate(str);
            if(mapper.selectById(pcm.getPath())!=null){
                mapper.update(pcm);
            }else {
                mapper.insert(pcm);
            }
            map.put("code",200);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("code",500);
            throw new RuntimeException();
        }
        return map;
    }


    public Map<String,Object> uploadDivPackage(MultipartFile file,String fileName,int seq){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //暂时存储至临时文件
            String Path = rootPath +"\\temporary\\"+fileName + "--" +seq;
            File dir = new File(rootPath+"\\temporary");
            if(!dir.exists()){
                dir.mkdirs();
            }
            System.out.println(Path);
            //输出到临时文件夹
            File outFile = new File(Path);
            file.transferTo(outFile);
            System.out.println("上传成功");
            map.put("code", 200);
        }catch (Exception e) {
            e.printStackTrace();
            map.put("code",500);
        }
        return map;
    }

}
