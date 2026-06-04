package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_book_info")
public class BookInfoDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String bookName;
    private String authorName;
    private String categoryName;
    private String coverUrl;
    private String description;
    private Integer finishedStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
