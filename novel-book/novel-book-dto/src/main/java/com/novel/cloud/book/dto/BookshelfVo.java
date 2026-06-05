package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Bookshelf list response DTO
 */
@Data
public class BookshelfVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<BookshelfItemVo> books;
    private Integer total;
}
