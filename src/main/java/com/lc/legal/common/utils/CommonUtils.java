package com.lc.legal.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author liangc
 * @version 1.0
 * @Description: 公共工具类
 * @date: 2021/7/24 18:36
 */
public class CommonUtils {

    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param image  Image对象
     * @param format image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage image, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 判断当前系统是windows还linux
     *
     * @return 是否为windows，true为windows，false为linux
     */
    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
