package com.novel.cloud.book.dto.response;

import com.novel.cloud.book.dto.vo.BookshelfItemVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Bookshelf list response
 */
@Data
public class BookshelfResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<BookshelfItemVo> books;

    private Integer total;
}
