package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Reading history query request
 */
@Data
public class ReadingHistoryQueryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "Page number must be at least 1")
    private Integer page = 1;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize = 20;
}
