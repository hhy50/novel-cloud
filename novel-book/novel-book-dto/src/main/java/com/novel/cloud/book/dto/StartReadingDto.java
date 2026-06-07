package com.novel.cloud.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 开始阅读请求 DTO。
 * chapterId 为可选：前端已选定章节时透传；未传则由后端按
 * "上次流水 -> 第一章" 顺序兜底。
 */
@Data
public class StartReadingDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId can not be null")
    private Long bookId;

    /** 可选：前端已确定要读的章节 ID，传则尊重 */
    private Long chapterId;
}
