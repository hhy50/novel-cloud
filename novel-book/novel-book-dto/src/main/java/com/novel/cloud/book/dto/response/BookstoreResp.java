package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookstoreSectionVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 书城首页响应
 */
@Data
public class BookstoreResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 顶部 Tab（可选） */
    private List<String> headerTabs;

    /** 分类区块列表（数据从 t_store_category_style 驱动） */
    private List<BookstoreSectionVo> sections;
}
