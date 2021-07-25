package com.lc.legal.controller;

import com.lc.legal.common.utils.FileUtils;
import io.swagger.annotations.Api;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * @author liangc
 * @version 1.0
 * @Description: PDF转EXCEL业务
 * @date: 2021/7/24 18:36
 */
@RestController
@RequestMapping(value = "/v1/api")
@Api(value = "PdfToExcelController", tags = {"PDF转EXCEL业务"}, hidden = true)
public class PdfToExcelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfToExcelController.class);


    @RequestMapping(value = "/aliBills", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> tansAliBillsToExcel(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        LOGGER.info("file name: {}", file.getOriginalFilename());
        LOGGER.info("file size: {}", file.getSize());
        LOGGER.info("file type: {}", file.getContentType());

        PDDocument doc = PDDocument.load(FileUtils.multipartFileToFile(file));
        // 获取总页数
        int pageNumber = doc.getNumberOfPages();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = null;

        // 文件按字节读取，然后按照UTF-8的格式编码显示
        writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);

        // 生成PDF文档内容剥离器
        PDFTextStripper stripper = new PDFTextStripper();
        // 排序
        stripper.setSortByPosition(true);
        // 设置转换的开始页
        stripper.setStartPage(1);
        // 设置转换的结束页
        stripper.setEndPage(pageNumber);
        try {
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=" + fileName + ".doc");
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
