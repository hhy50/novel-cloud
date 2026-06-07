package com.novel.cloud.book.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_book_info")
public class BookInfoDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 语言 */
    private String language;

    /** 作品名称 */
    private String name;

    /** 作者 */
    private String author;

    /** 作品简介 */
    private String description;

    /** 总定价 */
    private Integer totalPrice;

    /** 千字定价 */
    private Integer wordsPrice;

    /** 章节定价 */
    private Integer chapterPrice;

    /** 免费章节数 */
    private Integer freeChapters;

    /** 封面 */
    private String cover;

    /** 状态 1连载 2完结 3下架 */
    private Integer status;

    /** 上架状态 1上架 2下架 3逻辑下架 */
    private Integer onlineStatus;

    /** 是否会员作品 0非会员 1会员 */
    private Integer isBaoyue;

    /** 是否App限免数据 0否 1是 */
    private Integer isProductFreeLimit;

    /** 是否是热门作品 */
    private Integer isHot;

    /** 是否是新品 */
    private Integer isNew;

    /** 是否是限时免费作品 */
    private Integer isLimitedFree;

    /** 是否写手平台书籍 0否 1是 */
    private Integer isInkecho;

    /** 是否是爽文 */
    private Integer isYy;

    /** 是否是精选作品 */
    private Integer isGreatest;

    /** 是否是大神作品 */
    private Integer isGod;

    /** 总字数 */
    private Integer totalWords;

    /** 章节数 */
    private Integer totalChapters;

    /** 总浏览 */
    private Integer totalViews;

    /** 总收藏数 */
    private Integer totalFavors;

    /** 评分 */
    private Integer score;

    /** 最新章节ID */
    private Integer lastChapterId;

    /** 最新章节时间 (Unix timestamp) */
    private Integer lastChapterTime;

    /** 创建时间 (Unix timestamp) */
    private Integer createdAt;

    /** 更新时间 (Unix timestamp) */
    private Integer updatedAt;

    /** 删除时间 (Unix timestamp) */
    private Integer deletedAt;
}
