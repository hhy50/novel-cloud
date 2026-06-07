package com.novel.cloud.config.webmvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 通用请求日志拦截器
 *
 * 仅对带有 {@code @RequestMapping}（及其派生注解 GetMapping/PostMapping 等）
 * 的 Controller 方法生效（即 handler 为 {@link HandlerMethod} 的请求），
 * 在请求开始时打印请求信息，在请求结束时输出响应状态与耗时。
 */
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLogInterceptor.class);

    private static final String START_TIME_ATTR = "_REQ_LOG_START_TIME_";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullUri = (query == null || query.isBlank()) ? uri : uri + "?" + query;
        String handlerName = handlerMethod.getBeanType().getSimpleName()
                + "#" + handlerMethod.getMethod().getName();

        log.info("==> [{}] {} -> {}", method, fullUri, handlerName);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        Object startAttr = request.getAttribute(START_TIME_ATTR);
        long cost = (startAttr instanceof Long start) ? System.currentTimeMillis() - start : -1L;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        if (ex != null) {
            log.warn("<== [{}] {} status={} cost={}ms ex={}",
                    method, uri, status, cost, ex.getClass().getSimpleName());
        } else {
            log.info("<== [{}] {} status={} cost={}ms", method, uri, status, cost);
        }
    }
}
