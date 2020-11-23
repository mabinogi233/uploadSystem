package com.cqujava.uploadfile.Services.fileAdm;

import com.cqujava.uploadfile.Services.fileAdm.filePCM;

public interface filePCMMapper {
    int insert(filePCM record);

    int insertSelective(filePCM record);

    filePCM selectById(String path);

    void delete(String path);

    void update(filePCM record);
}