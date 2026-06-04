package com.novel.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_reading_stats")
public class ReadingStatsDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate statDate;

    private Integer booksRead;

    private Integer chaptersRead;

    private Integer minutesRead;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
