package com.novel.cloud.user.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user_feedback")
public class UserFeedbackDO {

    @TableId(type = IdType.AUTO)
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
