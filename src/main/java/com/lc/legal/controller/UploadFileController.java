package com.lc.legal.controller;

import com.alibaba.fastjson.JSONObject;
import com.lc.legal.common.bean.ResultCode;
import com.lc.legal.common.bean.ResultJson;
import com.lc.legal.common.utils.CommonUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liangc
 * @version 1.0
 * @Description: 文件上传业务
 * @date: 2021/7/24 18:36
 */
@RestController
@RequestMapping(value = "/v1/api")
@Api(value = "UploadFileController", tags = {"文件上传业务"}, hidden = true)
public class UploadFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileController.class);

    /**
     * 单文件上传功能测试
     *
     * @param file 文件列表
     * @return 返回值
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JSONObject uploadFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", file.getOriginalFilename());
        jsonObject.put("size", file.getSize());
        jsonObject.put("type", file.getContentType());
        return jsonObject;
    }


    /**
     * 多文件上传功能测试
     *
     * @param files 文件列表
     * @return 返回值
     */
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public JSONObject uploadFiles(@RequestParam("files") MultipartFile[] files) {
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            LOGGER.info("file name: {}", file.getOriginalFilename());
            LOGGER.info("file size: {}", file.getSize());
            LOGGER.info("file type: {}", file.getContentType());
            JSONObject fileObj = new JSONObject();
            fileObj.put("name", file.getOriginalFilename());
            fileObj.put("size", file.getSize());
            fileObj.put("type", file.getContentType());
            fileList.add(fileObj);
        }
        jsonObject.put("fileList", fileList);
        return jsonObject;
    }

    /**
     * 上传模板文件
     *
     * @param file 文件
     * @return 返回值
     */
    @RequestMapping(value = "/upload/uploadTemplateFile", method = RequestMethod.POST)
    public ResultJson uploadTemplateFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            ResultJson.failure(ResultCode.BAD_REQUEST);
        }

        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());

        String templateFilePath;
        if (CommonUtils.isWindows()) {
            templateFilePath = "E:\\opt\\cloud\\template\\";
        } else {
            templateFilePath = "/opt/cloud/template/";
        }

        File dest = new File(templateFilePath + file.getOriginalFilename());

        try {
            file.transferTo(dest);
            LOGGER.info("上传成功");
        } catch (IOException e) {
            LOGGER.error("上传失败: {}", e.getMessage());
        }

        return ResultJson.ok();
    }
}
