package com.novel.cloud.consts;

import lombok.Getter;

public enum BizErrType {

    INVALID_TOKEN(100010, "Invalid Token"),

    ;

    @Getter
    final int code;

    @Getter
    final String message;

    BizErrType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
