package com.novel.user.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * User feedback domain entity
 */
@Data
public class UserFeedback {

    private Long id;
    private Long userId;
    private String category;
    private String content;
    private String contact;
    private String images;
    private Integer status;
    private String reply;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
