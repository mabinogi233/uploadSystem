package com.cqujava.uploadfile.Services.fileAdm;


import com.cqujava.uploadfile.BeanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class fileAdm {

    @Autowired
    filePCMMapper mapper;

    static String rootPath = BeanConfig.rootPath;

    /**
     * 删除用户上传的文件
     * @param upath
     * @return
     */
    public boolean deleteFileAndPCM(String upath){
        try {
            File file = new File(rootPath+"\\"+upath);
            file.delete();
            mapper.delete(rootPath+"\\"+upath);
            if (mapper.selectById(rootPath+"\\"+upath) == null) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }


    /**
     * 查询用户上传的所有文件
     * @param uid
     * @return
     */
    public List<Map<String,String>> getFilesMessage(int uid){
        //查询用户上传的文件
        String path = rootPath + "\\" + Integer.toString(uid);
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        try{
            File file = new File(path);
            if(file.exists()){
                //该用户上传的文件
                File[] uploadfiles = file.listFiles();
                for(File uploadfile:uploadfiles){
                    //查询file的控制信息，返回文件列表
                    filePCM pcm= mapper.selectById(uploadfile.getAbsolutePath());
                    Map<String,String> one_file = new HashMap<>();
                    //加入文件信息
                    one_file.put("name",uploadfile.getName());
                    one_file.put("size",fileAdm.FormetFileSize(uploadfile.length()));
                    one_file.put("lastEditDate",pcm.getLasteditdate());
                    one_file.put("uploadDate",pcm.getUploaddate());
                    list.add(one_file);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }





    /**
     * 文件大小转换
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 修改文件信息
     * @param uid
     * @param fileNewName
     * @param fileOldName
     * @return
     */

    public boolean updateFile(int uid, String fileNewName,String fileOldName){
        String oldPath = rootPath + "\\" + Integer.toString(uid) +"\\" +fileOldName;
        String newPath = rootPath + "\\" + Integer.toString(uid) +"\\" +fileNewName;
        //最后修改时间改为当前
        Date date = new Date();//获取当前的日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String str = df.format(date);//获取String类型的时间
        //找到旧文件的PCM
        filePCM oldPCM = mapper.selectById(oldPath);
        if(mapper.selectById(newPath)!=null){
            //存在重名文件
            return false;
        }
        filePCM newPCM = new filePCM();
        newPCM.setLasteditdate(str);
        newPCM.setUploaddate(oldPCM.getUploaddate());
        newPCM.setPath(newPath);
        try {
            //原子操作
            File oldFile = new File(oldPath);
            File newfile = new File(newPath);
            boolean flag = oldFile.renameTo(newfile);
            if(flag){
                //加入新的PCM
                mapper.insert(newPCM);
                //删除旧的PCM
                mapper.delete(oldPath);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }


}
