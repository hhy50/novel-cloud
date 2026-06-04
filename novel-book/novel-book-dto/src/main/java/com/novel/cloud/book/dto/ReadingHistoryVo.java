package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Reading history response DTO
 */
@Data
public class ReadingHistoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<ReadingHistoryItemVo> records;
    private Integer total;
}
