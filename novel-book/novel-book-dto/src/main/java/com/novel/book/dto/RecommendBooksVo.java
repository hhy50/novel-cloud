package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Recommended books response DTO
 */
@Data
public class RecommendBooksVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<RecommendCard> cards;

    @Data
    public static class RecommendCard implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String title;
        private String tagMode;
        private List<RecommendBookItem> items;
    }

    @Data
    public static class RecommendBookItem implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long bookId;
        private String title;
        private String subtitle;
        private String coverUrl;
        private String coverHexColor;
    }
}
