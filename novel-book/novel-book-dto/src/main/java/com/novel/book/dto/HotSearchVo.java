package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Hot search keywords response DTO
 */
@Data
public class HotSearchVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<HotSearchItem> items;

    @Data
    public static class HotSearchItem implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Integer rank;
        private String keyword;
        private Integer searchCount;
    }
}
