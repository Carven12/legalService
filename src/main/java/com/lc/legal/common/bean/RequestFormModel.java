package com.lc.legal.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liangc
 * @version 1.0
 * @Description: 请求表单实体类
 * @date: 2021/7/24 18:36
 */
@Data
@AllArgsConstructor
public class RequestFormModel {

    public RequestFormModel() {
        paramMap = new HashMap<>();
        fileMap = new HashMap<>();
    }

    /**
     * 普通表单MAP
     */
    private Map<String, String> paramMap;

    /**
     * file表单MAP
     */
    private Map<String, FileItem> fileMap;
}
