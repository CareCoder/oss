package com.yq.oss.exception;

import com.yq.oss.common.DataResult;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private String code;
    private String message;

    public BusinessException(String message) {
        this.code = DataResult.STATUS_FAIL;
        this.message = message;
    }

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
