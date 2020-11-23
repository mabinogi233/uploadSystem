package com.cqujava.uploadfile.Services.fileUpLoad;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Component
public class MyListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session) {
        this.session = session;
        session.setAttribute("uploadPercent", 0);
    }

    @Override
    public void update(long l, long l1, int i) {
        //当前上传的 字节数*100
        long percent = (long) (l * 100.0);
        session.setAttribute("uploadPercent", percent);
    }
}
