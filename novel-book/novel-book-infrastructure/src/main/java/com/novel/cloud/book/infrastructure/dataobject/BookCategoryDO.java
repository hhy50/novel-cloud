package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_book_category")
public class BookCategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 书籍ID */
    private Long bookId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除时间 */
    private LocalDateTime deleteTime;
}
