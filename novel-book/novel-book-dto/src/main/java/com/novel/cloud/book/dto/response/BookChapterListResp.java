package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookChapterVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Book chapter list response
 */
@Data
public class BookChapterListResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;

    private List<BookChapterVo> chapters;
}
