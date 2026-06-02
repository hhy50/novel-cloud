package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class BookstoreQueryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tabCode;
}
