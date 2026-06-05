package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BookstoreVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> headerTabs;

    private List<BookstoreSectionVo> sections;
}
