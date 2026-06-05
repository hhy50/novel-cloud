package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Search books response DTO
 */
@Data
public class SearchBooksVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<SearchBookItemVo> books;
    private Integer total;
    private String keyword;
}
