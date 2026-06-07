package com.novel.cloud.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类，基于 Jackson
 *
 * @author haiyang
 */
@Slf4j
public final class JSONUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 忽略未知属性
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许空 Bean 序列化
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 注册 JavaTimeModule 以支持 Java 8 时间类型
        MAPPER.registerModule(new JavaTimeModule());
        // 禁用将日期写为时间戳
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private JSONUtil() {
    }

    /**
     * 获取 ObjectMapper 实例（可进行自定义配置后通过其他方法使用）
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * 对象转 JSON 字符串
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字符串失败", e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 对象转格式化 JSON 字符串
     */
    public static String toJSONStringPretty(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转格式化JSON字符串失败", e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * JSON 字符串转对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转对象失败, json={}, class={}", json, clazz.getName(), e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * JSON 字符串转复杂类型（如 List、Map 等泛型类型）
     * <pre>
     *     List<User> users = JSONUtil.parseObject(json, new TypeReference&lt;List&lt;User&gt;&gt;() {});
     * </pre>
     */
    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转复杂类型失败, json={}", json, e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * JSON 字符串转 List
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
            return MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转List失败, json={}, class={}", json, clazz.getName(), e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * 对象转 Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object object) {
        if (object == null) {
            return null;
        }
        return MAPPER.convertValue(object, Map.class);
    }

    /**
     * JSON 字符串转 Map
     */
    public static Map<String, Object> toMap(String json) {
        return parseObject(json, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * 对象转换（类型转换，如 DTO 转 VO）
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return MAPPER.convertValue(source, targetClass);
    }

    /**
     * 检查字符串是否为合法 JSON
     */
    public static boolean isValidJSON(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }
        try {
            MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

}
