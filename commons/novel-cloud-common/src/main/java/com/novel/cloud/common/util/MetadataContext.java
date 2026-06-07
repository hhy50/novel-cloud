package com.novel.cloud.common.util;

/**
 * 请求元数据上下文（ThreadLocal 实现）
 *
 * 由 RequestMetadataInterceptor 在请求进入时填充，在请求结束时清除。
 * 业务代码可在任意位置通过静态方法获取当前请求的客户端信息。
 *
 * <pre>
 * // 使用示例
 * String deviceId = MetadataContext.getDeviceId();
 * Long userId = MetadataContext.getUserId();
 * </pre>
 */
public final class MetadataContext {

    private MetadataContext() {}

    private static final ThreadLocal<Metadata> CONTEXT = ThreadLocal.withInitial(Metadata::new);

    // ──────────────── 填充方法（仅供拦截器调用） ────────────────

    public static void setDeviceId(String deviceId) {
        CONTEXT.get().deviceId = deviceId;
    }

    public static void setDeviceType(String deviceType) {
        CONTEXT.get().deviceType = deviceType;
    }

    public static void setPlatform(String platform) {
        CONTEXT.get().platform = platform;
    }

    public static void setAppVersion(String appVersion) {
        CONTEXT.get().appVersion = appVersion;
    }

    public static void setOsVersion(String osVersion) {
        CONTEXT.get().osVersion = osVersion;
    }

    public static void setTimeZone(String timeZone) {
        CONTEXT.get().timeZone = timeZone;
    }

    public static void setAppCode(String appCode) {
        CONTEXT.get().appCode = appCode;
    }

    public static void setSign(String sign) {
        CONTEXT.get().sign = sign;
    }

    public static void setTimestamp(String timestamp) {
        CONTEXT.get().timestamp = timestamp;
    }

    public static void setUserId(Long userId) {
        CONTEXT.get().userId = userId;
    }

    public static void setToken(String token) {
        CONTEXT.get().token = token;
    }

    public static void setLanguage(String language) {
        CONTEXT.get().language = language;
    }

    // ──────────────── 读取方法（业务代码使用） ────────────────

    public static String getDeviceId() {
        return CONTEXT.get().deviceId;
    }

    public static String getDeviceType() {
        return CONTEXT.get().deviceType;
    }

    public static String getPlatform() {
        return CONTEXT.get().platform;
    }

    public static String getAppVersion() {
        return CONTEXT.get().appVersion;
    }

    public static String getOsVersion() {
        return CONTEXT.get().osVersion;
    }

    public static String getTimeZone() {
        return CONTEXT.get().timeZone;
    }

    public static String getAppCode() {
        return CONTEXT.get().appCode;
    }

    public static String getSign() {
        return CONTEXT.get().sign;
    }

    public static String getTimestamp() {
        return CONTEXT.get().timestamp;
    }

    public static Long getUserId() {
        return CONTEXT.get().userId;
    }

    public static String getToken() {
        return CONTEXT.get().token;
    }

    public static String getLanguage() {
        return CONTEXT.get().language;
    }

    // ──────────────── 生命周期 ────────────────

    /** 请求结束后必须调用，防止内存泄漏 */
    public static void clear() {
        CONTEXT.remove();
    }

    // ──────────────── 内部存储 ────────────────

    private static class Metadata {
        String deviceId;
        String deviceType;
        String platform;
        String appVersion;
        String osVersion;
        String timeZone;
        String appCode;
        String sign;
        String timestamp;
        Long userId;
        String token;
        String language;
    }
}
