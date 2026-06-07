package com.novel.cloud.common.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final int code;

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
