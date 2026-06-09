package com.ruoyi.common.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache工具类（临时简化版，使用内存缓存替代Shiro缓存）
 * TODO: 建议后续改用Spring Cache或Redis
 * 
 * @author ruoyi
 */
@Deprecated
public class CacheUtils
{
    private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    // 使用ConcurrentHashMap作为简单的内存缓存
    private static final Map<String, Map<String, Object>> CACHE_MAP = new ConcurrentHashMap<>();

    private static final String SYS_CACHE = "sys-cache";

    /**
     * 获取SYS_CACHE缓存
     * 
     * @param key
     * @return
     */
    public static Object get(String key)
    {
        return get(SYS_CACHE, key);
    }

    /**
     * 获取SYS_CACHE缓存
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(String key, Object defaultValue)
    {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 写入SYS_CACHE缓存
     * 
     * @param key
     * @return
     */
    public static void put(String key, Object value)
    {
        put(SYS_CACHE, key, value);
    }

    /**
     * 从SYS_CACHE缓存中移除
     * 
     * @param key
     * @return
     */
    public static void remove(String key)
    {
        remove(SYS_CACHE, key);
    }

    /**
     * 获取缓存
     * 
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key)
    {
        Map<String, Object> cache = CACHE_MAP.get(cacheName);
        if (cache == null)
        {
            return null;
        }
        return cache.get(getKey(key));
    }

    /**
     * 获取缓存
     * 
     * @param cacheName
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(String cacheName, String key, Object defaultValue)
    {
        Object value = get(cacheName, getKey(key));
        return value != null ? value : defaultValue;
    }

    /**
     * 写入缓存
     * 
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value)
    {
        Map<String, Object> cache = CACHE_MAP.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());
        cache.put(getKey(key), value);
    }

    /**
     * 从缓存中移除
     * 
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key)
    {
        Map<String, Object> cache = CACHE_MAP.get(cacheName);
        if (cache != null)
        {
            cache.remove(getKey(key));
        }
    }

    /**
     * 从缓存中移除所有
     * 
     * @param cacheName
     */
    public static void removeAll(String cacheName)
    {
        Map<String, Object> cache = CACHE_MAP.get(cacheName);
        if (cache != null)
        {
            Set<String> keys = cache.keySet();
            cache.clear();
            logger.info("清理缓存： {} => {}", cacheName, keys);
        }
    }

    /**
     * 从缓存中移除指定key
     * 
     * @param keys
     */
    public static void removeByKeys(Set<String> keys)
    {
        removeByKeys(SYS_CACHE, keys);
    }

    /**
     * 从缓存中移除指定key
     * 
     * @param cacheName
     * @param keys
     */
    public static void removeByKeys(String cacheName, Set<String> keys)
    {
        for (Iterator<String> it = keys.iterator(); it.hasNext();)
        {
            remove(cacheName, it.next());
        }
        logger.info("清理缓存： {} => {}", cacheName, keys);
    }

    /**
     * 获取缓存键名
     * 
     * @param key
     * @return
     */
    private static String getKey(String key)
    {
        return key;
    }

    /**
     * 获得一个Cache，没有则创建
     * 
     * @param cacheName
     * @return
     */
    public static SimpleCache getCache(String cacheName)
    {
        Map<String, Object> cache = CACHE_MAP.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());
        return new SimpleCache(cache);
    }

    /**
     * 获取所有缓存名称
     * 
     * @return 缓存组
     */
    public static String[] getCacheNames()
    {
        Set<String> keySet = CACHE_MAP.keySet();
        return keySet.toArray(new String[0]);
    }

    /**
     * 简单缓存实现（模拟Shiro Cache接口）
     */
    public static class SimpleCache
    {
        private final Map<String, Object> cache;

        public SimpleCache(Map<String, Object> cache)
        {
            this.cache = cache;
        }

        public Object get(String key)
        {
            return cache.get(key);
        }

        public void put(String key, Object value)
        {
            cache.put(key, value);
        }

        public void remove(String key)
        {
            cache.remove(key);
        }

        public void clear()
        {
            cache.clear();
        }

        public Set<String> keys()
        {
            return cache.keySet();
        }

        public int size()
        {
            return cache.size();
        }
    }
}
