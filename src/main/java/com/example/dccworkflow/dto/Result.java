package com.example.dccworkflow.dto;

import com.example.dccworkflow.enums.ResultType;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T body;

    private Result() {}

    private Result(Integer code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public static <T> Result<T> of(ResultType resultType, T body) {
        return new Result<>(resultType.getCode(),
                resultType.getMessage(),
                body);
    }
}
