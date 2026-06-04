package com.novel.book.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.book.infrastructure.dataobject.UserBookshelfDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserBookshelfMapper extends BaseMapper<UserBookshelfDO> {

    @Update("UPDATE t_user_bookshelf SET last_chapter_id = #{chapterId}, last_read_time = NOW() WHERE user_id = #{userId} AND book_id = #{bookId}")
    void updateLastRead(@Param("userId") Long userId, @Param("bookId") Long bookId, @Param("chapterId") Long chapterId);
}
