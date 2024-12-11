package com.sky.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * AliOSS上传工具类(▲:当前情况下未注入到IOC中 并没有标记@Component) 需要单独编写Configuration类注入
 */
@Component
public class AliOssUtil {

    /**
     * 基于字节数组完成文件上传
     *
     * @param objectName 上传到桶中文件的名称
     * @return 文件上传后的URL访问路径
     */
    public boolean upload(MultipartFile multipartFile, String objectName){
        File fileDir = new File(System.getProperty("user.dir")+"\\image");
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return false;
            }
        }
        File file = new File(fileDir.getAbsolutePath() +"/"+objectName);
        if (file.exists()) {
            if (!file.delete()) {
                return false;
            }
        }
        try {
            if (file.createNewFile()) {
                multipartFile.transferTo(file);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
