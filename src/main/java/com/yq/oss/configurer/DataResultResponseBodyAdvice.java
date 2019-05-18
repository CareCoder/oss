package com.yq.oss.configurer;

import com.yq.oss.common.DataResult;
import com.yq.oss.common.PageResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

@ControllerAdvice
public class DataResultResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        Object value = bodyContainer.getValue();
        if (!(value instanceof DataResult) && !(value instanceof PageResponse)) {
            bodyContainer.setValue(new DataResult().success(value));
        }
    }
}
