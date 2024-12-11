package com.sky.controller.admin;

import cn.hutool.core.util.IdUtil;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CommonController
 * DESCRIPTION : 全局通用的表现层
 */
@Slf4j
@Api(tags = "B端-通用表现层接口")
@RequestMapping("/admin/common")
@RestController
public class CommonController {

    //注入容器中的AliOss上传工具类的对象
    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件到本地存储存储
     *
     * @param file 接收到要上传的文件
     * @return 存储中的访问URL路径(响应给前台 : 前台基于路径就可以访问图片并且展示出来)
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(MultipartFile file) throws IOException {
        //生成一个在桶中不重复的文件名称
        String originalFilename = file.getOriginalFilename();
        String suffixFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String bucketFileName = IdUtil.simpleUUID().concat(suffixFileName);
        aliOssUtil.upload(file, bucketFileName);
        return Result.success("http://localhost:8081/admin/common/download?fileName="+bucketFileName);
    }

    /**
     * 文件下载
     * @param fileName
     * @param response
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName,
                         HttpServletResponse response) throws IOException {
        File fileDir = new File(System.getProperty("user.dir")+"\\image");
        File image=new File(fileDir.getAbsolutePath() +"/"+fileName);
        if (image.exists()){
            FileInputStream fileInputStream=new FileInputStream(image);
            byte[] bytes=new byte[fileInputStream.available()];
            if (fileInputStream.read(bytes)>0){
                OutputStream outputStream=response.getOutputStream();
                outputStream.write(bytes);
                outputStream.close();
            }
            fileInputStream.close();
        }
    }
}
