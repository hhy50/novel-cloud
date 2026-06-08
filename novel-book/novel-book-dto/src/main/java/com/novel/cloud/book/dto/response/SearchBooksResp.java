package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.SearchBookItemVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Search books response
 */
@Data
public class SearchBooksResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<SearchBookItemVo> books;
    private Integer total;
    private String keyword;
}
