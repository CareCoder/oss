package com.yq.oss.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private int code;
    private String msg;
    private Integer count;
    private List<T> data;

    public static <T> PageResponse<T> build (Integer count, List<T> data) {
        return build(0, "", count, data);
    }

    public static <T> PageResponse<T> build (int code, String msg, Integer count, List<T> data) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setCode(code);
        pageResponse.setMsg(msg);
        pageResponse.setCount(count);
        pageResponse.setData(data);
        return pageResponse;
    }
}
