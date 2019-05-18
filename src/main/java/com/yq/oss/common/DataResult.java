package com.yq.oss.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataResult<T> implements Serializable {

    public static final String STATUS_SUCCESS = "0";
    public static final String STATUS_FAIL = "-1";
    public static final boolean IS_SUCCESS = true;

    private String code;
    private boolean success;
    private String message;
    private T data;

    public DataResult() {
    }

    public DataResult fail(String code, String message) {
        this.code = code;
        this.success = false;
        this.message = message;
        return this;
    }

    public DataResult fail(String message) {
        this.code = STATUS_FAIL;
        this.success = false;
        this.message = message;
        return this;
    }

    public DataResult<T> success(T data) {
        this.code = STATUS_SUCCESS;
        this.success = IS_SUCCESS;
        this.data = data;
        return this;
    }

    public DataResult success() {
        this.code = STATUS_SUCCESS;
        this.success = IS_SUCCESS;
        return this;
    }

    public DataResult fail() {
        this.code = STATUS_FAIL;
        this.success = false;
        return this;
    }
}
