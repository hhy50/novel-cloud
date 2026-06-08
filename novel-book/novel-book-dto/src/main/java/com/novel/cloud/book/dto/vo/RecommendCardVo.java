package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 推荐卡片（共用）
 */
@Data
public class RecommendCardVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private String tagMode;
    private List<RecommendBookItemVo> items;
}
