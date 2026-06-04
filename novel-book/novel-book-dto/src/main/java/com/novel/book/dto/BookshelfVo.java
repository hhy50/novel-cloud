package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

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
