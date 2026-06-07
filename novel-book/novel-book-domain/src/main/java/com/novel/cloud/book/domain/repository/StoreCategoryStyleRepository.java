package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.StoreCategoryStyle;

import java.util.List;

/**
 * 书城分类样式仓储接口
 */
public interface StoreCategoryStyleRepository {

    /**
     * 查询指定 App + 语言的顶层分类（pid IS NULL）
     */
    List<StoreCategoryStyle> findRootCategories(String appCode, String language);

    /**
     * 查询指定 pid 的子分类
     */
    List<StoreCategoryStyle> findByPid(Long pid);
}
