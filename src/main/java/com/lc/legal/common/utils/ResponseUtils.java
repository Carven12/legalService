package com.lc.legal.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {

    /**
     * 返回成功JSON
     *
     * @param json
     * @param response
     * @throws IOException
     */
    public static void backSuccessResp(JSONObject json, HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }
}
