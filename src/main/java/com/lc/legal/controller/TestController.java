package com.lc.legal.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangc
 * @version 1.0
 * @Description: 测试业务接口
 * @date: 2021/7/24 18:36
 */
@RestController
@RequestMapping(value = "/v1/api")
@Api(value = "测试业务接口1", tags = {"测试业务"}, hidden = true)
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ApiOperation(value = "接口测试", notes = "接口测试11111")
    public String hello() {
        LOGGER.info("HelloWorld!");
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/testRestful/{id}", method = RequestMethod.GET)
    public JSONObject testRestful(@PathVariable String id) {
        LOGGER.info("id: {}", id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "success");
        return jsonObject;
    }


}
