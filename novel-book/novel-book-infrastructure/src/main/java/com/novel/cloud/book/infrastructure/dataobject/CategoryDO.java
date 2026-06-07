package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_category")
public class CategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类名 */
    private String name;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除时间 */
    private LocalDateTime deleteTime;
}
