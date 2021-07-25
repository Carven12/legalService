package com.lc.legal.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liangc
 * @version 1.0
 * @Description: 身份证工具类
 * @date: 2021/7/24 18:36
 */
public class IdCardUtils {
    /**
     * 15位身份证号
     */
    private static final Integer FIFTEEN_ID_CARD = 15;

    /**
     * 18位身份证号
     */
    private static final Integer EIGHTEEN_ID_CARD = 18;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 根据身份证号获取性别
     *
     * @param idCard 身份证号
     * @return 性别
     */
    public static String getSex(String idCard) {
        String sex = "";
        if (StringUtils.isNotBlank(idCard)) {
            //15位身份证号
            if (idCard.length() == FIFTEEN_ID_CARD) {
                if (Integer.parseInt(idCard.substring(14, 15)) % 2 == 0) {
                    sex = "女";
                } else {
                    sex = "男";
                }
                //18位身份证号
            } else if (idCard.length() == EIGHTEEN_ID_CARD) {
                // 判断性别
                if (Integer.parseInt(idCard.substring(16).substring(0, 1)) % 2 == 0) {
                    sex = "女";
                } else {
                    sex = "男";
                }
            }
        }
        return sex;
    }

    /**
     * 根据身份证号获取年龄
     *
     * @param idCard 号码内容
     * @return 年龄
     */
    public static Integer getAge(String idCard) {
        int age = 0;
        Date date = new Date();
        if (StringUtils.isNotBlank(idCard) && isValid(idCard)) {
            //15位身份证号
            if (idCard.length() == FIFTEEN_ID_CARD) {
                // 身份证上的年份(15位身份证为1980年前的)
                String uyear = "19" + idCard.substring(6, 8);
                // 身份证上的月份
                String uyue = idCard.substring(8, 10);
                // 当前年份
                String fyear = format.format(date).substring(0, 4);
                // 当前月份
                String fyue = format.format(date).substring(5, 7);
                if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) {
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
                    // 当前用户还没过生
                } else {
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
                }
                //18位身份证号
            } else if (idCard.length() == EIGHTEEN_ID_CARD) {
                // 身份证上的年份
                String year = idCard.substring(6).substring(0, 4);
                // 身份证上的月份
                String yue = idCard.substring(10).substring(0, 2);
                // 当前年份
                String fyear = format.format(date).substring(0, 4);
                // 当前月份
                String fyue = format.format(date).substring(5, 7);
                // 当前月份大于用户出身的月份表示已过生日
                if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) {
                    age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
                    // 当前用户还没过生日
                } else {
                    age = Integer.parseInt(fyear) - Integer.parseInt(year);
                }
            }
        }
        return age;
    }

    /**
     * 获取出生日期  yyyy年MM月dd日
     *
     * @param idCard 号码内容
     * @return 出生年月日
     */
    public static String getBirthday(String idCard) {
        String birthday = "";
        String year = "";
        String month = "";
        String day = "";
        if (StringUtils.isNotBlank(idCard)) {
            //15位身份证号
            if (idCard.length() == FIFTEEN_ID_CARD) {
                // 身份证上的年份(15位身份证为1980年前的)
                year = "19" + idCard.substring(6, 8);
                //身份证上的月份
                month = idCard.substring(8, 10);
                //身份证上的日期
                day = idCard.substring(10, 12);
                //18位身份证号
            } else if (idCard.length() == EIGHTEEN_ID_CARD) {
                // 身份证上的年份
                year = idCard.substring(6).substring(0, 4);
                // 身份证上的月份
                month = idCard.substring(10).substring(0, 2);
                //身份证上的日期
                day = idCard.substring(12).substring(0, 2);
            }
            birthday = year + "年" + month + "月" + day + "日";
        }
        return birthday;
    }

    /**
     * 身份证验证
     *
     * @param idCard 号码内容
     * @return 是否有效
     */
    public static boolean isValid(String idCard) {
        boolean validResult = true;
        //校验长度只能为15或18
        int len = idCard.length();
        if (len != FIFTEEN_ID_CARD && len != EIGHTEEN_ID_CARD) {
            validResult = false;
        }
        //校验生日
        if (!validDate(idCard)) {
            validResult = false;
        }
        return validResult;
    }

    /**
     * 校验生日
     *
     * @param idCard 身份证号
     * @return 出生日期是否有效
     */
    private static boolean validDate(String idCard) {
        try {
            if (idCard.length() < 15) {
                return false;
            }

            String birth = idCard.length() == 15 ? "19" + idCard.substring(6, 12) : idCard.substring(6, 14);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthDate = sdf.parse(birth);
            if (!birth.equals(sdf.format(birthDate))) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
