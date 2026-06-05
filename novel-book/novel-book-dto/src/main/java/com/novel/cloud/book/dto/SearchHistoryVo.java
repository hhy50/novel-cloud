package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Search history response DTO
 */
@Data
public class SearchHistoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> keywords;
}
