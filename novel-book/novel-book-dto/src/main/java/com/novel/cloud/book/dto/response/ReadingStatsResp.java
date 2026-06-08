package com.novel.cloud.book.dto.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Reading statistics response
 */
@Data
public class ReadingStatsResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer booksRead;
    private Integer chaptersRead;
    private Integer minutesRead;
    private Integer daysActive;
}
