package com.lc.legal.common.utils;

import com.lc.legal.common.bean.FileEntity;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author liangc
 * @version 1.0
 * @Description: 文件处理工具类
 * @date: 2021/7/24 18:36
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (null == file || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 获取流文件
     *
     * @param ins  输入文件流
     * @param file 文件
     */
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file 文件
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /**
     * 生成word文件
     *
     * @param templateFile 模板文件位置
     * @param contentMap   替换内容
     * @return 文件流
     */
    public static ByteArrayOutputStream generateDoc(String templateFile, Map<String, String> contentMap) throws IOException {

        InputStream inputStream = new FileInputStream(templateFile);
        XWPFDocument document = WordUtils.changWordForCommon(inputStream, contentMap);

        //导出到文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        byteArrayOutputStream.close();
        return byteArrayOutputStream;
    }


    /**
     * 批量压缩文件
     *
     * @param fileEntityList 文件列表
     * @return zip文件流
     * @throws IOException IO异常
     */
    public static ByteArrayOutputStream zipFile(List<FileEntity> fileEntityList) throws IOException {
        //1.创建字节数组输出流，用于返回压缩后的输出流字节数组
        ByteArrayOutputStream zipStream = new ByteArrayOutputStream();

        //2.创建压缩输出流
        ZipOutputStream zipOut = new ZipOutputStream(zipStream);

        fileEntityList.forEach(fileEntity -> {
            try {
                zipOut.putNextEntry(new ZipEntry(fileEntity.getFileName()));
                zipOut.write(fileEntity.getByteArrayOutputStream().toByteArray());
                fileEntity.getByteArrayOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        zipOut.close();

        return zipStream;
    }
}
