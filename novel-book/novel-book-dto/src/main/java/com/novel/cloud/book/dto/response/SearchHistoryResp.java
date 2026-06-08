package com.novel.cloud.book.dto.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Search history response
 */
@Data
public class SearchHistoryResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> keywords;
}
