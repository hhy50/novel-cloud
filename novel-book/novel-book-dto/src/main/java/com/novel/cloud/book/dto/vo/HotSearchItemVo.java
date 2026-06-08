package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 热门搜索项（共用）
 */
@Data
public class HotSearchItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer rank;
    private String keyword;
    private Integer searchCount;
}
