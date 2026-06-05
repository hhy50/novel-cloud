package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BookstoreQueryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tabCode;
}
