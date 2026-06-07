package com.novel.cloud.book.infrastructure.sharding;

/**
 * 章节内容表分表路由器。
 * <p>规则：按 book_id 取模 10，路由到 n_chapter_content_0 ~ n_chapter_content_9。
 * 一本书的所有章节落在同一张物理表，便于按书查询、批量导入与缓存。</p>
 *
 * <p>表名由 book_id 计算，bookId 必为非空且非负；调用方在落库前必须已校验。
 * 物理表名只允许 [a-z0-9_] 字符，由本工具内部拼装常量后缀产生，不接受外部输入，
 * 因此可安全地拼入 SQL（MyBatis ${}）。</p>
 */
public final class ChapterContentShardingRouter {

    /** 分表数量，与 n_chapter_content_0..9 物理表保持一致 */
    public static final int SHARD_COUNT = 10;

    /** 表名前缀 */
    private static final String TABLE_PREFIX = "n_chapter_content_";

    private ChapterContentShardingRouter() {
    }

    /**
     * 计算 bookId 对应的物理表名。
     */
    public static String tableName(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("bookId must not be null for chapter content sharding");
        }
        // 用 Math.floorMod 避免负数 bookId 导致 -idx
        int idx = Math.floorMod(bookId, SHARD_COUNT);
        return TABLE_PREFIX + idx;
    }
}
