package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 书城区块（共用）
 */
@Data
public class BookstoreSectionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer style;

    private String title;

    private String subtitle;

    private List<String> tags;

    private Boolean showViewAll;

    private List<BookstoreBookVo> books;
}
