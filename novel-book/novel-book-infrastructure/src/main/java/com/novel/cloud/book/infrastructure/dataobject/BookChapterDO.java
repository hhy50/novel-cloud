package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍章节 DO (对齐 n_book_chapter 表)
 */
@Data
@TableName("n_book_chapter")
public class BookChapterDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 书籍ID */
    private Long bookId;

    /** 章节标题 */
    private String title;

    /** 字数 */
    private Integer wordsCount;

    /** 是否是VIP章节 */
    private Integer isVip;

    /** 章节序号 */
    private Integer number;

    /** 是否发布 1发布 0未发布 */
    private Integer status;

    /** 章节价格（单位：分），-1表示使用书籍的每章价格，0表示免费，>0表示该章节的特定价格 */
    private Integer price;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
