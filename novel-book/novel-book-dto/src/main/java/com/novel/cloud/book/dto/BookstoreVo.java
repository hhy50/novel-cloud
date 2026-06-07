package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 书城首页响应
 *
 * <pre>
 * {
 *   "sections": [
 *     { "style": 1, "title": "编辑推荐", "tags": ["hot"], "books": [...] }
 *   ]
 * }
 * </pre>
 */
@Data
public class BookstoreVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 顶部 Tab（可选） */
    private List<String> headerTabs;

    /** 分类区块列表（数据从 t_store_category_style 驱动） */
    private List<BookstoreSectionVo> sections;
}
