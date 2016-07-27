package com.gaige.mylibrary;

/**
 * Created by gaoqun on 2016/4/20.
 */
public class ResponseData<T> {
    private String code;
    private boolean successed;
    private T data;
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return successed;
    }

    public void setSuccess(boolean success) {
        this.successed = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
