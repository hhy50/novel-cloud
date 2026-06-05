package com.novel.book.adapter.handler;

import com.novel.common.core.domain.R;
import com.novel.common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Mono<R<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Mono.just(R.fail(e.getCode(), e.getMessage()));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<R<Void>> handleValidationException(WebExchangeBindException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));

        log.warn("参数校验失败: {}", message);
        return Mono.just(R.fail(400, message));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<R<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return Mono.just(R.fail(400, e.getMessage()));
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public Mono<R<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return Mono.just(R.fail(500, "系统错误，请稍后重试"));
    }
}
