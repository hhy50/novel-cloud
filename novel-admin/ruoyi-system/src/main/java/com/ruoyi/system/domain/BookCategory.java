package com.ruoyi.system.domain;

import jakarta.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小说分类对象 t_category
 *
 * @author ruoyi
 */
public class BookCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @Excel(name = "分类ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String name;

    /** 删除标记（0存在 2删除） */
    private String delFlag;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "分类名称不能为空")
    @Size(min = 0, max = 50, message = "分类名称长度不能超过50个字符")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
