package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.RecommendCardVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Recommended books response
 */
@Data
public class RecommendBooksResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<RecommendCardVo> cards;
}
