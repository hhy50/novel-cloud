package com.novel.common.core.domain;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    public static <T> R<T> ok() {
        return R.<T>builder()
                .code(0)
                .message("success")
                .build();
    }

    public static <T> R<T> ok(T data) {
        return R.<T>builder()
                .code(0)
                .message("success")
                .data(data)
                .build();
    }

    public static <T> R<T> fail(String message) {
        return R.<T>builder()
                .code(1)
                .message(message)
                .build();
    }

    public static <T> R<T> fail(Integer code, String message) {
        return R.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}
