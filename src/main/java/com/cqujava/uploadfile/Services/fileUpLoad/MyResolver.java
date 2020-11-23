package com.cqujava.uploadfile.Services.fileUpLoad;



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MyResolver extends CommonsMultipartResolver {

    @Autowired
    private MyListener listener;

    /**
     * 解析mutipart文件
     * @param request
     * @return
     * @throws MultipartException
     */

    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = this.determineEncoding(request);
        FileUpload fileUpload = this.prepareFileUpload(encoding);

        //****************************************************************************
        //添加上传监听器，设置上传限制
        //此处应大于前端分片PDU=50MB，接受到的为 PCI + PDU > 50MB
        fileUpload.setFileSizeMax(1024 * 1024 * 60);// 单个文件最大60M
        fileUpload.setSizeMax(1024 * 1024 * 60);// 一次提交总文件最大60M
        listener.setSession(request.getSession());
        fileUpload.setProgressListener(listener);
        //****************************************************************************

        try {
            List<FileItem> fileItems = ((ServletFileUpload)fileUpload).parseRequest(request);
            return this.parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException var5) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), var5);
        } catch (FileUploadBase.FileSizeLimitExceededException var6) {
            throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), var6);
        } catch (FileUploadException var7) {
            throw new MultipartException("Failed to parse multipart servlet request", var7);
        }
    }
}
