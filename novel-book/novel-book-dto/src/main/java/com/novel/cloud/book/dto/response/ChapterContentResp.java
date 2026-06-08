package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookChapterVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 章节内容响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChapterContentResp extends BookChapterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String content;
}
