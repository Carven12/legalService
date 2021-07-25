package com.lc.legal.controller;


import com.lc.legal.common.bean.FileEntity;
import com.lc.legal.common.utils.FileUtils;
import com.lc.legal.service.GenerateWordService;
import io.swagger.annotations.Api;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liangc
 * @version 1.0
 * @Description: 批量生成律师函
 * @date: 2021/7/24 18:36
 */
@RestController
@RequestMapping(value = "/v1/api")
@Api(value = "GenerateWordController", tags = {"批量生成律师函业务"}, hidden = true)
public class GenerateWordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateWordController.class);

    @Autowired
    private GenerateWordService generateWordService;

    @RequestMapping(value = "/generateWord", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> generate(@RequestParam("file") MultipartFile file) throws Exception {
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());
        InputStream in = new FileInputStream(FileUtils.multipartFileToFile(file));

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);

        // 生成的文件流List
        List<FileEntity> outputStreamList = new ArrayList<>();

        for (int i = 0; i < sheets.getNumberOfSheets(); i++) {
            outputStreamList.addAll(generateWordService.getWordStreamList(sheets.getSheetAt(i)));
        }

        // 关闭文件流
        sheets.close();

        // 批量压缩文件
        ByteArrayOutputStream byteArrayOutputStream = FileUtils.zipFile(outputStreamList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=document.zip");
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
