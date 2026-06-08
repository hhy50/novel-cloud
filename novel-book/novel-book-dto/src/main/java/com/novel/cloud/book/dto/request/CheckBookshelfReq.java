package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 检查书籍是否在书架请求
 */
@Data
public class CheckBookshelfReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "书籍ID不能为空")
    private Long bookId;
}
