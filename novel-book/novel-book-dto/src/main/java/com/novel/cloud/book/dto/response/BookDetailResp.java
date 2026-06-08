package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookChapterVo;
import com.novel.cloud.book.dto.vo.BookDetailVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 书籍详情响应（BookDetailVo 字段 + 章节列表）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookDetailResp extends BookDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 章节列表（含标题，不含正文） */
    private List<BookChapterVo> chapters;
}
