package com.novel.cloud.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 检查书籍是否在书架响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckBookshelfResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 书籍ID
     */
    private Long bookId;

    /**
     * 是否在书架中
     */
    private Boolean inBookshelf;
}
