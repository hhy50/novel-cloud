package com.novel.gateway.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 网关 TraceId 过滤器
 *
 * 入口处生成或继承 traceId：
 * <ul>
 *   <li>从请求头 {@code X-Trace-Id} 取值，没有则生成一个 32 位无连字符 UUID；</li>
 *   <li>透传到下游服务（重写请求头）；</li>
 *   <li>回写到响应头，便于客户端排查；</li>
 *   <li>写入 MDC，让网关自身日志带 traceId（WebFlux 单线程多请求复用，
 *       Mono 完成后立即清理）。</li>
 * </ul>
 */
@Component
public class TraceIdGlobalFilter implements GlobalFilter, Ordered {

    public static final String HEADER_NAME = "X-Trace-Id";
    public static final String MDC_KEY = "traceId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst(HEADER_NAME);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        String finalTraceId = traceId;

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(HEADER_NAME, finalTraceId)
                .build();
        exchange.getResponse().getHeaders().set(HEADER_NAME, finalTraceId);

        MDC.put(MDC_KEY, finalTraceId);
        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .doFinally(signal -> MDC.remove(MDC_KEY));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
