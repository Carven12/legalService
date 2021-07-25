package com.lc.legal.controller;

import com.lc.legal.common.utils.CommonUtils;
import com.lc.legal.common.utils.VerifyCode;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

/**
 * @author liangc
 * @version 1.0
 * @Description: 文件下载业务接口
 * @date: 2021/7/24 18:36
 */
@RestController
@RequestMapping(value = "/v1/api")
@Api(value = "DownLoadController", tags = {"文件下载业务接口"}, hidden = true)
public class DownLoadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownLoadController.class);

    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getVerifyCode() {
        String verifyCode = VerifyCode.genRandomCode();
        LOGGER.info("verifyCode: {}", verifyCode);
        BufferedImage image = VerifyCode.genVerifyCodeImage(verifyCode);
        byte[] imgByte = CommonUtils.imageToBytes(image, "png");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=demo.png");
        return new ResponseEntity<byte[]>(imgByte, headers, HttpStatus.CREATED);
    }

}
