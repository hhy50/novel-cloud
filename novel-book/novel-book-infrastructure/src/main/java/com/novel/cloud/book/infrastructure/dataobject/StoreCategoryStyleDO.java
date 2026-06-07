package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_store_category_style")
public class StoreCategoryStyleDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父级所属ID */
    private Long pid;

    /** 所属App代码 */
    private String appCode;

    /** 语言 */
    private String language;

    /** 分类名 */
    private String name;

    /** 展示样式 */
    private Integer styleCode;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除时间 */
    private LocalDateTime deleteTime;
}
