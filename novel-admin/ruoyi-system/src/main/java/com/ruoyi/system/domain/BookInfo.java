package com.ruoyi.system.domain;

import jakarta.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小说信息对象 t_book_info
 *
 * @author ruoyi
 */
public class BookInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小说ID */
    @Excel(name = "小说ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 语言 */
    @Excel(name = "语言")
    private String language;

    /** 作品名称 */
    @Excel(name = "作品名称")
    private String name;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 作品简介 */
    @Excel(name = "作品简介")
    private String description;

    /** 总定价 */
    @Excel(name = "总定价", cellType = ColumnType.NUMERIC)
    private Integer totalPrice;

    /** 千字定价 */
    @Excel(name = "千字定价", cellType = ColumnType.NUMERIC)
    private Integer wordsPrice;

    /** 章节定价 */
    @Excel(name = "章节定价", cellType = ColumnType.NUMERIC)
    private Integer chapterPrice;

    /** 免费章节数 */
    @Excel(name = "免费章节数", cellType = ColumnType.NUMERIC)
    private Integer freeChapters;

    /** 封面 */
    @Excel(name = "封面")
    private String cover;

    /** 状态 1连载 2完结 3下架 */
    @Excel(name = "状态", readConverterExp = "1=连载,2=完结,3=下架")
    private Integer status;

    /** 上架状态 1上架 2下架 3逻辑下架 */
    @Excel(name = "上架状态", readConverterExp = "1=上架,2=下架,3=逻辑下架")
    private Integer onlineStatus;

    /** 是否会员作品 0非会员 1会员 */
    @Excel(name = "是否会员作品", readConverterExp = "0=否,1=是")
    private Integer isBaoyue;

    /** 是否热门作品 0否 1是 */
    @Excel(name = "是否热门", readConverterExp = "0=否,1=是")
    private Integer isHot;

    /** 是否新品 0否 1是 */
    @Excel(name = "是否新品", readConverterExp = "0=否,1=是")
    private Integer isNew;

    /** 是否限时免费 0否 1是 */
    @Excel(name = "是否限时免费", readConverterExp = "0=否,1=是")
    private Integer isLimitedFree;

    /** 是否精选 0否 1是 */
    @Excel(name = "是否精选", readConverterExp = "0=否,1=是")
    private Integer isGreatest;

    /** 是否大神作品 0否 1是 */
    @Excel(name = "是否大神作品", readConverterExp = "0=否,1=是")
    private Integer isGod;

    /** 总字数 */
    @Excel(name = "总字数", cellType = ColumnType.NUMERIC)
    private Integer totalWords;

    /** 章节数 */
    @Excel(name = "章节数", cellType = ColumnType.NUMERIC)
    private Integer totalChapters;

    /** 总浏览 */
    @Excel(name = "总浏览", cellType = ColumnType.NUMERIC)
    private Integer totalViews;

    /** 总收藏数 */
    @Excel(name = "总收藏数", cellType = ColumnType.NUMERIC)
    private Integer totalFavors;

    /** 评分 */
    @Excel(name = "评分", cellType = ColumnType.NUMERIC)
    private Integer score;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    @NotBlank(message = "作品名称不能为空")
    @Size(min = 0, max = 100, message = "作品名称长度不能超过100个字符")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @NotBlank(message = "作者不能为空")
    @Size(min = 0, max = 50, message = "作者长度不能超过50个字符")
    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    @Size(min = 0, max = 2000, message = "作品简介长度不能超过2000个字符")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public Integer getWordsPrice()
    {
        return wordsPrice;
    }

    public void setWordsPrice(Integer wordsPrice)
    {
        this.wordsPrice = wordsPrice;
    }

    public Integer getChapterPrice()
    {
        return chapterPrice;
    }

    public void setChapterPrice(Integer chapterPrice)
    {
        this.chapterPrice = chapterPrice;
    }

    public Integer getFreeChapters()
    {
        return freeChapters;
    }

    public void setFreeChapters(Integer freeChapters)
    {
        this.freeChapters = freeChapters;
    }

    public String getCover()
    {
        return cover;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getOnlineStatus()
    {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus)
    {
        this.onlineStatus = onlineStatus;
    }

    public Integer getIsBaoyue()
    {
        return isBaoyue;
    }

    public void setIsBaoyue(Integer isBaoyue)
    {
        this.isBaoyue = isBaoyue;
    }

    public Integer getIsHot()
    {
        return isHot;
    }

    public void setIsHot(Integer isHot)
    {
        this.isHot = isHot;
    }

    public Integer getIsNew()
    {
        return isNew;
    }

    public void setIsNew(Integer isNew)
    {
        this.isNew = isNew;
    }

    public Integer getIsLimitedFree()
    {
        return isLimitedFree;
    }

    public void setIsLimitedFree(Integer isLimitedFree)
    {
        this.isLimitedFree = isLimitedFree;
    }

    public Integer getIsGreatest()
    {
        return isGreatest;
    }

    public void setIsGreatest(Integer isGreatest)
    {
        this.isGreatest = isGreatest;
    }

    public Integer getIsGod()
    {
        return isGod;
    }

    public void setIsGod(Integer isGod)
    {
        this.isGod = isGod;
    }

    public Integer getTotalWords()
    {
        return totalWords;
    }

    public void setTotalWords(Integer totalWords)
    {
        this.totalWords = totalWords;
    }

    public Integer getTotalChapters()
    {
        return totalChapters;
    }

    public void setTotalChapters(Integer totalChapters)
    {
        this.totalChapters = totalChapters;
    }

    public Integer getTotalViews()
    {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews)
    {
        this.totalViews = totalViews;
    }

    public Integer getTotalFavors()
    {
        return totalFavors;
    }

    public void setTotalFavors(Integer totalFavors)
    {
        this.totalFavors = totalFavors;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("language", getLanguage())
            .append("name", getName())
            .append("author", getAuthor())
            .append("description", getDescription())
            .append("totalPrice", getTotalPrice())
            .append("wordsPrice", getWordsPrice())
            .append("chapterPrice", getChapterPrice())
            .append("freeChapters", getFreeChapters())
            .append("cover", getCover())
            .append("status", getStatus())
            .append("onlineStatus", getOnlineStatus())
            .append("isBaoyue", getIsBaoyue())
            .append("isHot", getIsHot())
            .append("isNew", getIsNew())
            .append("isLimitedFree", getIsLimitedFree())
            .append("isGreatest", getIsGreatest())
            .append("isGod", getIsGod())
            .append("totalWords", getTotalWords())
            .append("totalChapters", getTotalChapters())
            .append("totalViews", getTotalViews())
            .append("totalFavors", getTotalFavors())
            .append("score", getScore())
            .append("categoryId", getCategoryId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
