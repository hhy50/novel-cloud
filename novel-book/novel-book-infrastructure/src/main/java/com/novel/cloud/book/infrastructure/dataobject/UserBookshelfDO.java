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
    /** 用户ID */
    private Long userId;

    /** 书籍ID */
    private Long bookId;

    /** 最后阅读的章节ID */
    private Long lastChapterId;

    private LocalDateTime lastReadTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
