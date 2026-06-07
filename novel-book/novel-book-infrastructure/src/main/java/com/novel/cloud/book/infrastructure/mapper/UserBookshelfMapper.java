package com.novel.cloud.book.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.book.infrastructure.dataobject.UserBookshelfDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserBookshelfMapper extends BaseMapper<UserBookshelfDO> {

    @Update("UPDATE t_user_bookshelf SET last_chapter_id = #{chapterId}, last_read_time = NOW() WHERE user_id = #{userId} AND book_id = #{bookId}")
    void updateLastRead(@Param("userId") Long userId, @Param("bookId") Long bookId, @Param("chapterId") Long chapterId);

    /**
     * 隐式加入书架：依赖 uk_user_book 唯一索引做 upsert，
     * 已存在则刷新 last_chapter_id / last_read_time / update_time。
     * 一条 SQL 完成，避免应用层 "查→插→重试" 的竞态。
     */
    @org.apache.ibatis.annotations.Insert(
            "INSERT INTO t_user_bookshelf (user_id, book_id, last_chapter_id, last_read_time, create_time, update_time) " +
                    "VALUES (#{userId}, #{bookId}, #{chapterId}, NOW(), NOW(), NOW()) " +
                    "ON DUPLICATE KEY UPDATE last_chapter_id = VALUES(last_chapter_id), " +
                    "last_read_time = NOW(), update_time = NOW()"
    )
    void upsert(@Param("userId") Long userId, @Param("bookId") Long bookId, @Param("chapterId") Long chapterId);
}
