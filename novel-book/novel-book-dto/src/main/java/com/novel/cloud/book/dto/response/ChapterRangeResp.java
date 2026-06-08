package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookChapterVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 章节范围响应
 */
@Data
public class ChapterRangeResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 当前章节信息 */
    private BookChapterVo currentChapter;

    /** 前面的章节列表（按序号升序） */
    private List<BookChapterVo> previousChapters;

    /** 后面的章节列表（按序号升序） */
    private List<BookChapterVo> nextChapters;

    /** 总章节数 */
    private Integer totalChapters;
}
