package com.hyd.hydrogenpac.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract result
 *
 * @author yidin
 */
public class Result {

    private boolean success;

    private String message = "";

    private Map<String, Object> data = new HashMap<>();

    public static Result success() {
        return new Result(true);
    }

    public static Result fail(String message) {
        return new Result(false, message);
    }

    public Result() {
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
