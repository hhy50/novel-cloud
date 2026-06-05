package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user_bookshelf")
public class UserBookshelfDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long bookId;

    private Long lastChapterId;

    private LocalDateTime lastReadTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
