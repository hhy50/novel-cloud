package com.novel.user.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

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
