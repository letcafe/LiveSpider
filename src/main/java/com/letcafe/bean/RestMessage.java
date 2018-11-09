package com.letcafe.bean;

import io.swagger.annotations.ApiModelProperty;

public class RestMessage<T> {
    @ApiModelProperty(value = "响应码")
    private Integer code;
    @ApiModelProperty(value = "对响应码的相关描述信息")
    private String msg;
    @ApiModelProperty(value = "数据信息")
    private T data;

    public RestMessage() {
    }

    public RestMessage(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public RestMessage<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RestMessage<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public RestMessage<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "RestMessage{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
