package com.yq.oss.configurer;

import com.yq.oss.common.DataResult;
import com.yq.oss.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public DataResult exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("Error in {}: {}", e.getClass().getSimpleName(), e);
        if (e instanceof BusinessException) {
            BusinessException e1 = (BusinessException) e;
            return new DataResult().fail(e1.getCode(), e1.getMessage());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            return new DataResult().fail(error.getDefaultMessage());
        } else {
            return new DataResult().fail(e.getMessage());
        }
    }
}
