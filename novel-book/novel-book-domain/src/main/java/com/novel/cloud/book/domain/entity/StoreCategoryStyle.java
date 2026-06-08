package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书城分类样式 - 领域实体
 */
@Data
public class StoreCategoryStyle {

    private Long id;
    private Long pid;
    private String appCode;
    private String language;
    private String name;
    private Integer styleCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;

    /**
     * 判断是否是根分类
     */
    public boolean isRootCategory() {
        return pid == null || pid == 0;
    }

    /**
     * 判断是否有效
     */
    public boolean isValid() {
        return deleteTime == null;
    }

    /**
     * 获取样式名称
     */
    public String getStyleName() {
        if (styleCode == null) {
            return "未知";
        }
        return switch (styleCode) {
            case 1 -> "Tab切换";
            case 2 -> "Hero半透明";
            case 3 -> "排行榜";
            case 4 -> "横滚";
            case 5 -> "双列Hero";
            case 6 -> "首本大卡+横滚";
            case 7 -> "纵向书单";
            case 8 -> "横幅+横滚";
            default -> "未知样式";
        };
    }
}
