package com.activity.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service
public class FileUploadService {
    // 定义图片存储路径
    private static final String UPLOAD_DIRECTORY = "uploads/";

    public String uploadImage(MultipartFile file) throws IOException {
        // 生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 创建存储文件的路径
        File destFile = new File(UPLOAD_DIRECTORY + fileName);

        // 确保目录存在
        destFile.getParentFile().mkdirs();

        // 将文件保存到指定路径
        file.transferTo(destFile);

        // 返回文件存储的路径
        return UPLOAD_DIRECTORY + fileName;
    }
}
