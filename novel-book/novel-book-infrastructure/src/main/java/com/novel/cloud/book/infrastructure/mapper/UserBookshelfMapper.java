package com.novel.cloud.book.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.cloud.book.infrastructure.dataobject.UserBookshelfDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

public interface UserBookshelfMapper extends BaseMapper<UserBookshelfDO> {

    @Update("UPDATE t_user_bookshelf SET last_chapter_id = #{chapterId}, last_read_time = NOW() WHERE user_id = #{userId} AND book_id = #{bookId}")
    void updateLastRead(@Param("userId") Long userId, @Param("bookId") Long bookId, @Param("chapterId") Long chapterId);

    /**
     * 加入书架：先查询，存在则更新，不存在则插入
     */
    default UserBookshelfDO upsert(@Param("userId") Long userId, @Param("bookId") Long bookId) {
        UserBookshelfDO shelfDo = selectOne(new LambdaQueryWrapper<UserBookshelfDO>()
                .eq(UserBookshelfDO::getUserId, userId)
                .eq(UserBookshelfDO::getBookId, bookId));

        LocalDateTime now = LocalDateTime.now();
        if (shelfDo != null) {
            shelfDo.setLastReadTime(now);
            shelfDo.setUpdateTime(now);
            updateById(shelfDo);
        } else {
            shelfDo = new UserBookshelfDO();
            shelfDo.setUserId(userId);
            shelfDo.setBookId(bookId);
            shelfDo.setLastReadTime(now);
            shelfDo.setCreateTime(now);
            shelfDo.setUpdateTime(now);
            insert(shelfDo);
        }
        return shelfDo;
    }
}
