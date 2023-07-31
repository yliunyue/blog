package com.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.utils.QiniuUtils;
import com.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file) {
        log.info("图片：{}",file.toString());
        //原始文件名称 aa.png  aa.png
        String originalFilename = file.getOriginalFilename();
        //substringAfterLast（,"."）得到.后面的  png
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        /*
         * 上传文件，上传到哪?  上传到七牛云，注意，只有一个月时间，后面要去改QiniuUtils里面的内容
         * 按量付费的，速度快，会把图片发送到离用户最近的服务器上
         * 降低自身应用服务器的带宽消耗
         * */
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload) {
            //上传成功，返回路径
            return Result.success(QiniuUtils.url + fileName);
        }
        //失败，返回失败信息
        return Result.fail(30003, "上传失败");
    }

    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public Result getToken() {
        // return Result.success(qiniuUtils.getNormalUploadToken());
        return Result.success(qiniuUtils.getToken());
    }
}
