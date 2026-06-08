package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 推荐书籍项（共用）
 */
@Data
public class RecommendBookItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String title;
    private String subtitle;
    private String coverUrl;
}
