package com.lc.legal.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liangc
 * @version 1.0
 * @Description: 返回实体类
 * @date: 2021/7/24 18:36
 */
@Data
public class ResultJson<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    public static ResultJson<?> ok() {
        return ok("");
    }

    public static ResultJson<Object> ok(Object o) {
        return new ResultJson<Object>(ResultCode.SUCCESS, o);
    }

    public static ResultJson<?> failure(ResultCode code) {
        return failure(code, "");
    }

    public static ResultJson<Object> failure(ResultCode code, Object o) {
        return new ResultJson<Object>(code, o);
    }

    public ResultJson(ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson(ResultCode resultCode, T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"' +
                '}';
    }
}
