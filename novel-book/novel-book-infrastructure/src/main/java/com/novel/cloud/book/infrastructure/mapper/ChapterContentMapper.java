package com.novel.cloud.book.infrastructure.mapper;

import com.novel.cloud.book.infrastructure.dataobject.ChapterContentDO;
import com.novel.cloud.book.infrastructure.sharding.ChapterContentShardingRouter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

/**
 * 章节内容 Mapper（n_chapter_content_0 ~ n_chapter_content_9 分表）。
 * <p>不继承 BaseMapper —— BaseMapper 依赖固定 @TableName，分表场景无法复用。
 * 物理表名由 {@link ChapterContentShardingRouter} 根据 bookId 计算后由 Provider 拼接。</p>
 */
@Mapper
public interface ChapterContentMapper {

    /**
     * 按 (book_id, id) 在指定分片表查询章节内容；
     * 同时传 bookId 用于路由 + 兜底 where，避免拿到错路由表里的同 id 数据。
     * 过滤 delete_time IS NULL 的逻辑删除。
     */
    @SelectProvider(type = ChapterContentSqlProvider.class, method = "selectByBookIdAndChapterId")
    ChapterContentDO selectByBookIdAndChapterId(@Param("bookId") Long bookId,
                                                @Param("chapterId") Long chapterId);

    /**
     * SQL Provider：根据 bookId 取模拼出物理表名。
     * <p>注意：表名由路由器从受信输入（bookId 计算结果）拼装常量后缀产生，
     * 仅含 [a-z0-9_]，不存在 SQL 注入风险；其余参数仍走 #{} 预编译。</p>
     */
    class ChapterContentSqlProvider implements ProviderMethodResolver {

        public String selectByBookIdAndChapterId(@Param("bookId") Long bookId,
                                                 @Param("chapterId") Long chapterId) {
            String table = ChapterContentShardingRouter.tableName(bookId);
            return "SELECT id, book_id, wordscount, content, create_time, update_time, delete_time "
                    + "FROM " + table + " "
                    + "WHERE id = #{chapterId} AND book_id = #{bookId} AND delete_time IS NULL "
                    + "LIMIT 1";
        }
    }
}
