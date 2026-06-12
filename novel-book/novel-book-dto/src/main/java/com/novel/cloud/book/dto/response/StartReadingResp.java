package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookChapterVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 开始阅读响应，提供前端进入阅读器所需的最小信息。
 * 章节正文不在此返回，前端拿到 chapterId 后单独调 /chapter/content。
 */
@Data
public class StartReadingResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 书籍 ID */
    private Long bookId;

    /** 最后一次阅读的章节信息 */
    private BookChapterVo lastedReadChapter;
}
