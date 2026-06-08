package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 搜索书籍项（共用）
 */
@Data
public class SearchBookItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String bookName;
    private String authorName;
    private String categoryName;
    private String coverUrl;
    private String description;

    /** 状态 1连载 2完结 3下架 */
    private Integer status;

    /** 兼容旧字段 */
    private Integer finishedStatus;

    /** 总字数 */
    private Integer totalWords;

    /** 评分 (0-10) */
    private Integer score;

    /** 是否热门 */
    private Integer isHot;

    /** 是否新品 */
    private Integer isNew;

    /** 是否会员 */
    private Integer isBaoyue;
}
