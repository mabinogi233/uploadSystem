package com.cqujava.uploadfile.Services.fileAdm;

public class filePCM {
    private String path;

    private String lasteditdate;

    private String uploaddate;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getLasteditdate() {
        return lasteditdate;
    }

    public void setLasteditdate(String lasteditdate) {
        this.lasteditdate = lasteditdate == null ? null : lasteditdate.trim();
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate == null ? null : uploaddate.trim();
    }
}