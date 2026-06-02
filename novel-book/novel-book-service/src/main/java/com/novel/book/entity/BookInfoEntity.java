package com.novel.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("book_info")
public class BookInfoEntity {

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
