package com.lc.legal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liangc
 * @version 1.0
 * @Description: springboot启动类
 * @date: 2021/7/24 18:36
 */
@SpringBootApplication
public class LegalApplication implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(LegalApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LegalApplication.class, args);
        logger.info("项目启动成功");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
