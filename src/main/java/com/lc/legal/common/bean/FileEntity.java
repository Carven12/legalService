package com.lc.legal.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * @author liangc
 * @version 1.0
 * @Description: 文件实体类
 * @date: 2021/7/24 18:36
 */
@Data
@AllArgsConstructor
public class FileEntity {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 字节流
     */
    private ByteArrayOutputStream byteArrayOutputStream;
}
