package com.lc.legal.common.utils;

import com.lc.legal.common.bean.RequestFormModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liangc
 * @version 1.0
 * @Description: Form表单请求工具类
 * @date: 2021/7/24 18:36
 */
public class FormRequestUtil {

    /**
     * 从request流中解析参数与上传的文件
     *
     * @param request 请求
     * @return 从form表单中提取的实体类
     */
    public static RequestFormModel parseParam(HttpServletRequest request) {

        RequestFormModel formModel = new RequestFormModel();

        //创建一个FileItem工厂 通过DiskFileItemFactory对象创建文件上传核心组件
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");

        try {
            //通过文件上传核心组件解析request请求，获取到所有的FileItem对象
            List<FileItem> fileItemList = upload.parseRequest(request);

            //遍历表单的所有表单项（FileItem） 并对其进行相关操作
            for (FileItem fileItem : fileItemList) {
                //判断这个表单项如果是一个普通的表单项
                if (fileItem.isFormField()) {
                    formModel.getParamMap().put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
                } else {
                    //如果不是表单的普通文本域，就是
                    formModel.getFileMap().put(fileItem.getName(), fileItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formModel;
    }
}
