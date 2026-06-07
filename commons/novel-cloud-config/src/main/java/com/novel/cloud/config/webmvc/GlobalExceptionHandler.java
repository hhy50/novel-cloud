package com.novel.cloud.config.webmvc;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.novel.cloud.common.domain.R;
import com.novel.cloud.common.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 配置在 commons/novel-cloud-config 中，所有引用该模块的服务自动生效。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 业务异常 ====================

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    // ==================== 参数校验异常 ====================

    /**
     * @Valid / @Validated 校验 RequestBody 失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fe) {
                        return fe.getField() + ": " + fe.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return R.fail(400, message);
    }

    /**
     * @Valid / @Validated 校验普通表单/Query参数失败
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fe) {
                        return fe.getField() + ": " + fe.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", message);
        return R.fail(400, message);
    }

    /**
     * 方法级校验 @Validated on controller 失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("约束校验失败: {}", message);
        return R.fail(400, message);
    }

    // ==================== 请求参数/格式异常 ====================

    /**
     * 缺少必填请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<Void> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("缺少必填参数: {}", e.getParameterName());
        return R.fail(400, "缺少必填参数: " + e.getParameterName());
    }

    /**
     * 参数类型转换失败
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型错误: {} 期望 {}", e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "?");
        return R.fail(400, "参数类型错误: " + e.getName());
    }

    /**
     * 请求体格式错误 / JSON 解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return R.fail(400, "请求体格式错误，请检查JSON格式");
    }

    /**
     * 非法参数
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<Void> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return R.fail(400, e.getMessage());
    }

    // ==================== HTTP 方法/路径异常 ====================

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("不支持的请求方法: {}", e.getMethod());
        return R.fail(405, "不支持的请求方法: " + e.getMethod());
    }

    /**
     * 路径不存在 (spring.mvc.throw-exception-if-no-handler-found=true 时生效)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<Void> handleNoHandlerFound(NoHandlerFoundException e) {
        log.warn("路径不存在: {} {}", e.getHttpMethod(), e.getRequestURL());
        return R.fail(404, "接口不存在");
    }

    /**
     * 静态资源不存在 (Spring Boot 默认会抛出此异常)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public R<Void> handleNoResourceFound(NoResourceFoundException e) {
        return R.fail(404, "资源不存在");
    }

    // ==================== 文件上传异常 ====================

    /**
     * 文件大小超限
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R<Void> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        log.warn("文件上传大小超限: {}", e.getMessage());
        return R.fail(400, "上传文件大小超出限制");
    }

    // ==================== Sa-Token 认证授权异常 ====================

    /**
     * 未登录
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLogin(NotLoginException e) {
        log.warn("未登录访问: {}", e.getMessage());
        return R.fail(401, "请先登录");
    }

    /**
     * 无权限
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleNotPermission(NotPermissionException e) {
        log.warn("权限不足: {}", e.getMessage());
        return R.fail(403, "权限不足");
    }

    /**
     * 无角色
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Void> handleNotRole(NotRoleException e) {
        log.warn("角色不匹配: {}", e.getMessage());
        return R.fail(403, "无权访问");
    }

    // ==================== 兜底系统异常 ====================

    /**
     * 兜底系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail(500, "系统错误，请稍后重试");
    }
}
