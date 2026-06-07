package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户阅读流水 DO（t_user_history）。
 * <p>注意：与 t_reading_history 不同，本表无 (user_id, book_id) 唯一约束，
 * 一次读取一行，按 create_time 写入。</p>
 */
@Data
@TableName("t_user_history")
public class UserHistoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long bookId;

    private Long chapterId;

    private Integer progress;

    private LocalDateTime createTime;
}
