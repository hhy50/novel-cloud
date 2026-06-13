package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.RecommendBookItemVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Similar books response for book detail page.
 */
@Data
public class BookSimilarBooksResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<RecommendBookItemVo> items;
}
