package com.novel.cloud.config.webmvc;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceId 过滤器
 *
 * 从请求头 {@code X-Trace-Id} 取链路 ID，若不存在则生成一个 32 位无连字符 UUID。
 * 写入 SLF4J MDC（key = {@link #MDC_KEY}），并回写到响应头 {@code X-Trace-Id}，
 * 方便日志聚合与跨服务排查。请求结束后清理 MDC，防止线程复用导致串值。
 *
 * <p>Order 设为最高，保证后续所有过滤器/拦截器/Controller 日志都带 traceId。
 */
public class TraceIdFilter extends OncePerRequestFilter implements Ordered {

    public static final String HEADER_NAME = "X-Trace-Id";
    public static final String MDC_KEY = "traceId";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String traceId = request.getHeader(HEADER_NAME);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        try {
            MDC.put(MDC_KEY, traceId);
            response.setHeader(HEADER_NAME, traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
