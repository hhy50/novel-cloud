package com.novel.cloud.config.webmvc;

import com.novel.cloud.common.util.MetadataContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求元数据拦截器
 *
 * 在每个 HTTP 请求进入 Controller 前，从请求头中提取客户端信息，
 * 填充到 MetadataContext 中。请求完成后自动清理，防止内存泄漏。
 *
 * <p>支持的请求头（与 Flutter DioClient 对齐）：
 * <ul>
 *   <li>X-App-Code      — 应用标识</li>
 *   <li>X-Timestamp     — 请求时间戳</li>
 *   <li>X-User-Id       — 用户ID</li>
 *   <li>X-Sign          — 签名</li>
 *   <li>X-Device-Id     — 设备ID</li>
 *   <li>X-Device-Type   — 设备类型</li>
 *   <li>X-Platform      — 平台</li>
 *   <li>X-App-Version   — App版本</li>
 *   <li>X-Os-Version    — 系统版本</li>
 *   <li>X-Time-Zone     — 时区</li>
 *   <li>Authorization   — Token</li>
 * </ul>
 */
public class RequestMetadataInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestMetadataInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        MetadataContext.setAppCode(request.getHeader("X-App-Code"));
        MetadataContext.setTimestamp(request.getHeader("X-Timestamp"));
        MetadataContext.setSign(request.getHeader("X-Sign"));
        MetadataContext.setDeviceId(request.getHeader("X-Device-Id"));
        MetadataContext.setDeviceType(request.getHeader("X-Device-Type"));
        MetadataContext.setPlatform(request.getHeader("X-Platform"));
        MetadataContext.setAppVersion(request.getHeader("X-App-Version"));
        MetadataContext.setOsVersion(request.getHeader("X-Os-Version"));
        MetadataContext.setTimeZone(request.getHeader("X-Time-Zone"));

        // 语言：优先 X-Language，其次 Accept-Language
        String lang = request.getHeader("X-Language");
        if (lang != null && !lang.isBlank()) {
            // 取第一个语言标签的主部分，如 "zh-CN,zh;q=0.9" → "zh"
            String primary = lang.split(",")[0].trim();
            int dashIdx = primary.indexOf('_');
            MetadataContext.setLanguage(dashIdx > 0 ? primary.substring(0, dashIdx) : primary);
        }

        // X-User-Id
        String userIdStr = request.getHeader("X-User-Id");
        if (userIdStr != null && !userIdStr.isBlank()) {
            try {
                MetadataContext.setUserId(Long.valueOf(userIdStr));
            } catch (NumberFormatException e) {
                log.warn("Invalid X-User-Id header: {}", userIdStr);
            }
        }

        // Token（从 Authorization 头提取）
        String auth = request.getHeader("Authorization");
        if (auth != null && !auth.isBlank()) {
            MetadataContext.setToken(auth);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        // 防止内存泄漏
        MetadataContext.clear();
    }
}
