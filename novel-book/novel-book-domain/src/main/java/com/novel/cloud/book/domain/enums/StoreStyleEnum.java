package com.novel.cloud.book.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 书城展示样式枚举（与 Flutter 端 BookstoreStyleFactory 一一对应）
 *
 * <p>数据库 t_store_category_style.style_code 存储此枚举的 code</p>
 */
@Getter
@AllArgsConstructor
public enum StoreStyleEnum {

    /**
     * 带 TabBar 切换（Hot / New / Top），下方横向滚动 5 本书
     */
    TABBED_GRID(1, "TabBar 切换 + 5 本横滚"),

    /**
     * 第一本大卡片（半透明背景圆角容器），其余 5 本横向滚动
     */
    HERO_SEMI_TRANSPARENT(2, "大卡半透明背景 + 5 本横滚"),

    /**
     * 排行榜风格：第一本带圆角背景 + 排名标签，其余 3 本 compact 模式竖向排列
     */
    RANKING_LIST(3, "排行榜风（首本排名 + 3 本 compact）"),

    /**
     * 纯横向滚动：固定宽度 item，展示 6 本书
     */
    HORIZONTAL_SCROLL(4, "纯横滚（6 本）"),

    /**
     * 前 2 本并排大卡片（Row 各占 Expanded），后 4 本横向滚动
     */
    TWO_COLUMN_HERO(5, "双列大卡 + 4 本横滚"),

    /**
     * 第一本大卡片 + 下方横向滚动 4 本书
     */
    HERO_HORIZONTAL(6, "首本大卡 + 4 本横滚"),

    /**
     * 纵向书单：所有书垂直排列，每行一条 BookTile，带排名序号
     */
    VERTICAL_LIST(7, "纵向书单（带排名）"),

    /**
     * 第一条横幅大卡片（渐变背景、书名 + 简介），其余书横向滚动
     */
    BANNER_SCROLL(8, "横幅大卡 + 横滚");

    /** 样式编号（对应 t_store_category_style.style_code） */
    private final int code;

    /** 中文描述 */
    private final String description;

    /**
     * 根据 code 获取枚举，找不到返回 null
     */
    public static StoreStyleEnum fromCode(int code) {
        for (StoreStyleEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
