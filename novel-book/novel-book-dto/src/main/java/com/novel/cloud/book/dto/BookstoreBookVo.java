package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BookstoreBookVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;

    private String title;

    private String author;

    private String description;

    /** 封面 URL */
    private String coverUrl;

    /** 是否为热门作品 */
    private Integer isHot;

    /** 是否为新品 */
    private Integer isNew;

    /** 是否为限时免费 */
    private Integer isLimitedFree;

    /** 是否为会员作品 */
    private Integer isBaoyue;

    /** 评分 (0-10) */
    private Integer score;

    /** 章节总数 */
    private Integer totalChapters;
}
