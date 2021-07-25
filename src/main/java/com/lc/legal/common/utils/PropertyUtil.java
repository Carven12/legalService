package com.lc.legal.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            //通过类加载器进行获取properties文件流
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("conf/config.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            LOGGER.error("config.properties文件未找到");
        } catch (IOException e) {
            LOGGER.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.error("config.properties文件流关闭出现异常");
            }
        }
    }

    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */
    private static String getValue(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }


    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 取出Integer类型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Long类型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Long getLong(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Long.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Double getDouble(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key);
        return value != null ? Boolean.valueOf(value) : defaultValue;
    }
}
