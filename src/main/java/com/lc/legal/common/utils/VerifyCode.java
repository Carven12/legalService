package com.lc.legal.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author liangc
 * @version 1.0
 * @Description: 验证码工具类
 * @date: 2021/7/24 18:36
 */
public class VerifyCode {
    /**
     * 验证码基础数据
     */
    private static final String BASE_NUM_LETTER = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * 验证码噪点数
     */
    private static final int NOISE = 30;

    /**
     * 验证码干扰线数
     */
    private static final int INTER_FAIENCE_LINE = 6;

    /**
     * 生成文本验证码
     *
     * @return 4位随机字符串
     */
    public static String genRandomCode() {
        StringBuilder sBuffer = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            int dot = random.nextInt(BASE_NUM_LETTER.length());
            sBuffer.append(BASE_NUM_LETTER.charAt(dot));
        }
        return sBuffer.toString();
    }

    /**
     * 生成验证码图片
     *
     * @param verifyCode 验证码
     * @return BufferedImage 文件流
     */
    public static BufferedImage genVerifyCodeImage(String verifyCode) {
        int width = 200;
        int height = 69;
        // 创建验证码画板
        BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) verifyImg.getGraphics();
        // 设置画笔颜色-验证码背景色
        graphics.setColor(Color.WHITE);
        // 填充背景
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font("微软雅黑", Font.BOLD, 40));
        // 旋转原点的 x 坐标
        int x = 10;
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < verifyCode.length(); i++) {
            graphics.setColor(getRandomColor());
            // 设置字体旋转角度 角度小于30度
            int degree = random.nextInt() % 30;
            // 正向旋转
            graphics.rotate(degree * Math.PI / 180, x, 45);
            graphics.drawString(String.valueOf(verifyCode.charAt(i)), x, 45);
            // 反向旋转
            graphics.rotate(-degree * Math.PI / 180, x, 45);
            x += 48;
        }

        // 画干扰线
        for (int i = 0; i < INTER_FAIENCE_LINE; i++) {
            // 设置随机颜色
            graphics.setColor(getRandomColor());
            // 随机画线
            graphics.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));
        }

        // 添加噪点
        for (int i = 0; i < NOISE; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x1, y1, 2, 2);
        }
        return verifyImg;
    }

    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));

    }
}
