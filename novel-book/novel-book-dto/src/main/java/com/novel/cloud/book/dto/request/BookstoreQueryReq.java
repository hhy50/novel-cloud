package com.novel.cloud.book.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Bookstore query request
 */
@Data
public class BookstoreQueryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tabCode;
}
