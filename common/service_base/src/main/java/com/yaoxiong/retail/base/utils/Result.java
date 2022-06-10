package com.yaoxiong.retail.base.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//Unified return result class
@Data
public class Result {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();


    private Result() {}


    public static Result ok() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(20000);
        result.setMessage("success");
        return result;
    }


    public static Result error() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(20001);
        result.setMessage("fail");
        return result;
    }

    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
