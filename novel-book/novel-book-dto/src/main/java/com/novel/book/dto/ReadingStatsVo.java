package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Reading statistics response DTO
 */
@Data
public class ReadingStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer booksRead;
    private Integer chaptersRead;
    private Integer minutesRead;
    private Integer daysActive;
}
