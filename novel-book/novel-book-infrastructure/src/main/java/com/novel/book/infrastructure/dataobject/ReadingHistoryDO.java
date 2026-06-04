package com.novel.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_reading_history")
public class ReadingHistoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long bookId;

    private Long chapterId;

    private Integer progress;

    private Integer duration;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
